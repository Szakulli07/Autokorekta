package com.example.imagepro;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ObjectDetector {

    private final Interpreter interpreter;
    private final float SCORE_THRESHOLD = 0.5f;

    private final List<String> labelList;
    private final int INPUT_SIZE;
    private final int PIXEL_SIZE = 3;
    private final int IMAGE_MEAN = 0;
    private final float IMAGE_STD = 255.0f;

    private int height;
    private int width;

    private List<float[]> predicts = new ArrayList<>();
    private boolean detecting = false;

    ObjectDetector(AssetManager assetManager, String modelPath, String labelPath, int inputSize) throws IOException {
        INPUT_SIZE = inputSize;

        // Initialize interpreter with GPU delegate
        Interpreter.Options options = new Interpreter.Options();
        CompatibilityList compatList = new CompatibilityList();

        if (compatList.isDelegateSupportedOnThisDevice()) {
            // if the device has a supported GPU, add the GPU delegate
            GpuDelegate.Options delegateOptions = compatList.getBestOptionsForThisDevice();
            GpuDelegate gpuDelegate = new GpuDelegate(delegateOptions);
            options.addDelegate(gpuDelegate);
        } else {
            // if the GPU is not supported, run on 4 threads
            options.setNumThreads(4);
        }

        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), options);
        labelList = loadLabelList(assetManager, labelPath);
    }

    public List<float[]> getPredicts() {
        return predicts;
    }

    public boolean isDetecting() {
        return detecting;
    }

    private List<String> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public List<Prediction> recognizeImage(Mat mat_image) {

        // Rotate original image by 90 degree to get portrait frame
        Mat rotated_mat_image = new Mat();
        Core.flip(mat_image.t(), rotated_mat_image, 1);

        // Convert to bitmap
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(rotated_mat_image.cols(), rotated_mat_image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rotated_mat_image, bitmap);

        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

        // Convert bitmap to bytebuffer for model input
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);

        // Defining output
        Object[] input = new Object[1];
        input[0] = byteBuffer;

        Map<Integer, Object> output_map = new TreeMap<>();

        float[][][] output = new float[1][25200][23];

        output_map.put(0, output);

        Log.d("CARS", "Just before predicting");

        // Predict
        interpreter.runForMultipleInputsOutputs(input, output_map);

        Log.d("CARS", "Just after predicting");

        Object predictsObject = output_map.get(0);
        float[][] predicts = ((float[][][]) predictsObject)[0];

        Log.d("CARS", "After casting");

        List<float[]> highScorePredicts = filterPredictsByScore(predicts, SCORE_THRESHOLD);
        List<Prediction> preNonMaxSuppressionPredicts = highScorePredicts.stream()
                                                                        .map(predict -> new Prediction(this.height, this.width, predict))
                                                                        .collect(Collectors.toList());
        return nonMaxSuppression(preNonMaxSuppressionPredicts, 0.5F);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer;
        int quant = 1;
        int size_image = INPUT_SIZE;
        if (quant == 0) {
            byteBuffer = ByteBuffer.allocateDirect(size_image * size_image * 3);
        } else {
            byteBuffer = ByteBuffer.allocateDirect(4 * size_image * size_image * 3);
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[size_image * size_image];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < size_image; ++i) {
            for (int j = 0; j < size_image; ++j) {
                final int val = intValues[pixel++];
                if (quant == 0) {
                    byteBuffer.put((byte) ((val >> 16) & 0xFF));
                    byteBuffer.put((byte) ((val >> 8) & 0xFF));
                    byteBuffer.put((byte) (val & 0xFF));
                } else {
                    byteBuffer.putFloat((((val >> 16) & 0xFF)) / 255.0f);
                    byteBuffer.putFloat((((val >> 8) & 0xFF)) / 255.0f);
                    byteBuffer.putFloat((((val) & 0xFF)) / 255.0f);
                }
            }
        }
        return byteBuffer;
    }

    private List<float[]> filterPredictsByScore(float[][] predicts, float score_threshold) {
        List<float[]> filteredPredicts = new ArrayList<>();
        for (float[] predict : predicts) {
            float score_value = predict[4];
            if (Float.compare(score_value, score_threshold)>0) {
                filteredPredicts.add(predict);
            }
        }
        return filteredPredicts;
    }

    private List<Prediction> nonMaxSuppression(List<Prediction> predicts, float iouThreshold) {
        List<Prediction> keep = new ArrayList<>();
        predicts.sort((l, r) -> Float.compare(l.getLabelScore(), r.getLabelScore()));

        while(!predicts.isEmpty()) {
            // Keep best box
            Prediction bestBox = predicts.get(predicts.size() - 1);
            predicts.remove(predicts.size() - 1);
            keep.add(bestBox);
            Log.d("DETECTED_CLASS", labelList.get((int) bestBox.getLabel()));

            // Remove overlapping boxes
            List<Integer> removeIndices = new ArrayList<>();
            for(int i=0; i<predicts.size(); i++){
                // Check if same class
                Prediction compareBox = predicts.get(i);
                if(!(isSmallSignClass(compareBox.getLabel()) ^ !isSmallSignClass(bestBox.getLabel()))){
                    continue;
                }

                float iou = bestBox.calculateIoU(compareBox);
                if(Float.compare(iou, iouThreshold)>0){
                    removeIndices.add(i);
                }
            }

            for(int i=removeIndices.size()-1; i>=0; i--){
                predicts.remove(removeIndices.get(i).intValue());
            }
        }
        return keep;
    }

    private boolean isSmallSignClass(int label){
        List<Integer> small = Arrays.asList( 4, 5, 11, 15, 17 );
        return small.contains(label);
    }
}
