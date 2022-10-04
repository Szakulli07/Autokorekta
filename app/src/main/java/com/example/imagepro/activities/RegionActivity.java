package com.example.imagepro.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.imagepro.Prediction;
import com.example.imagepro.R;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class RegionActivity extends DetectionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.region);
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

        for(Prediction prediction: predictions){
            Imgproc.rectangle(out,
                        new Point(prediction.getLeftX(), prediction.getDownY()),
                        new Point(prediction.getRightX(), prediction.getUpperY()),
                        new Scalar(255, 155, 155), 2);
        }
        // Rotate back by -90 degree
        Core.flip(out.t(), out, 0);

        return out;
    }

}