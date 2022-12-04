package com.example.imagepro.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.imagepro.Label;
import com.example.imagepro.R;
import com.example.imagepro.Tile;
import com.example.imagepro.cars.Car;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarActivity extends DetectionActivity {

    TextView bioScore;
    TextView hybScore;
    TextView eleScore;
    TextView solScore;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.car);

        this.bioScore = findViewById(R.id.bio_score);
        this.hybScore = findViewById(R.id.hyb_score);
        this.eleScore = findViewById(R.id.ele_score);
        this.solScore = findViewById(R.id.sol_score);

        bioScore.setText("B: " + 0);
        hybScore.setText("H: " + 0);
        eleScore.setText("E: " + 0);
        solScore.setText("S: " + 0);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();

        List<Car> cars = this.carGame.getCars();
        List<Tile> wrongTiles = this.carGame.getWrongTiles();

        if(this.carGame.accessCarGame()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    carGame.computeResults(mRgba);
                }
            };
            thread.start();
        }

        return this.drawPredicts(mRgba, cars, wrongTiles);
    }

    @SuppressLint("SetTextI18n")
    protected Mat drawPredicts(Mat in, List<Car> cars, List<Tile> wrongTiles){

        Map<Label, Integer> scores = new HashMap<Label, Integer>() {{
            put(Label.BLANK, 0);
            put(Label.BIOFUEL, 0);
            put(Label.HYBRID, 0);
            put(Label.ELECTRIC, 0);
            put(Label.SOLAR, 0);

        }};

        Mat out= new Mat();
        in.copyTo(out);

        for(Car car: cars){
            out = car.draw(out);

            if(car.isValid()){
                scores.replace(car.getType(), scores.get(car.getType())+1);
            }
        }

        Core.flip(out.t(), out, 1);

        for(Tile wrongTile: wrongTiles){
            if(wrongTile.getLabel() == Label.BLANK){
                Imgproc.putText(out, "XXX",
                        new Point(wrongTile.getCarPart().getLeftX(),
                                wrongTile.getCarPart().getUpperY()),
                        Core.FONT_HERSHEY_SIMPLEX,
                        2f, new Scalar(256, 256, 256), 2);
            }else{
                Imgproc.putText(out, "O",
                        new Point(wrongTile.getCarPart().getCenterX() - wrongTile.getSize() / 8f,
                                wrongTile.getCarPart().getCenterY() + wrongTile.getSize() / 8f),
                        Core.FONT_HERSHEY_SIMPLEX,
                        2f, new Scalar(256, 256, 256), 4);
            }
        }

        Core.flip(out.t(), out, 0);

        bioScore.setText("B: " + scores.get(Label.BIOFUEL));
        hybScore.setText("H: " + scores.get(Label.HYBRID));
        eleScore.setText("E: " + scores.get(Label.ELECTRIC));
        solScore.setText("S: " + scores.get(Label.SOLAR));

        return out;
    }

}