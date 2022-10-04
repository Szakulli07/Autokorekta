package com.example.imagepro.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.imagepro.Prediction;
import com.example.imagepro.R;
import com.example.imagepro.Tile;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Iterator;
import java.util.List;

public class LabelActivity extends DetectionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.label);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();

        List<Prediction> predictions = this.carGame.getPredictions();

        if(this.carGame.accessCarGame()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    carGame.computeResults(mRgba);
                }
            };
            thread.start();
        }

        return this.drawPredicts(mRgba, predictions);
    }

    protected Mat drawPredicts(Mat in, List<Prediction> predictions){

        if(predictions.isEmpty()){
            return  in;
        }

        // Rotate original image by 90 degree to get portrait frame
        Mat out=new Mat();
        Core.flip(in.t(), out, 1);

        Iterator colorIterator = colors.iterator();

        for(Prediction prediction: predictions){

            if(!colorIterator.hasNext())
                colorIterator = colors.iterator();

            Scalar color = (Scalar)colorIterator.next();

            Imgproc.putText(out, prediction.getLabel().toString(),
                    new Point(prediction.getLeftX(), prediction.getCenterY()),
                    Core.FONT_HERSHEY_SIMPLEX,
                    0.5f, color, 2);
        }
        // Rotate back by -90 degree
        Core.flip(out.t(), out, 0);

        return out;
    }

}