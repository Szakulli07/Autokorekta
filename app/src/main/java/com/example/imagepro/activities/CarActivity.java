package com.example.imagepro.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.imagepro.R;
import com.example.imagepro.cars.Car;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.util.List;

public class CarActivity extends DetectionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.car);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();

        List<Car> cars = this.carGame.getCars();

        if(this.carGame.accessCarGame()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    carGame.computeResults(mRgba);
                }
            };
            thread.start();
        }

        return this.drawPredicts(mRgba, cars);
    }

    protected Mat drawPredicts(Mat in, List<Car> cars){

        if(cars.isEmpty()){
            return  in;
        }

        Mat out= new Mat();
        in.copyTo(out);

        for(Car car: cars){
            out = car.draw(out);

        }

        return out;
    }

}