package com.example.imagepro.activities;

import android.util.Log;

import com.example.imagepro.Tile;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class RegionActivity extends DetectionActivity {

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();

        List<Tile> tiles = this.carGame.getResults();

        if(this.carGame.accessCarGame()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    carGame.computeResults(mRgba);
                }
            };
            thread.start();
        }

        Log.d("PREDS", String.valueOf(tiles.size()));

        return this.drawPredicts(mRgba, tiles);
    }

    @Override
    protected Mat drawPredicts(Mat in, List<Tile> tiles){

        if(tiles.isEmpty()){
            return  in;
        }

        // Rotate original image by 90 degree to get portrait frame
        Mat out=new Mat();
        Core.flip(in.t(), out, 1);

        for(Tile tile: tiles){
            for(Tile neighbourTile: tile.getNeighbours()){
                Imgproc.line(out,
                        new Point(tile.getCarPart().getCenterX(), tile.getCarPart().getCenterY()),
                        new Point(neighbourTile.getCarPart().getCenterX(), neighbourTile.getCarPart().getCenterY()),
                        new Scalar(255, 155, 155), 2);
            }
        }
        // Rotate back by -90 degree
        Core.flip(out.t(), out, 0);

        return out;
    }

}