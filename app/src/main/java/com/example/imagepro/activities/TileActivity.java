package com.example.imagepro.activities;

import android.os.Bundle;
import android.widget.TextView;

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

public class TileActivity extends DetectionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.title);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame){
        mRgba=inputFrame.rgba();

        List<Tile> tiles = this.carGame.getTiles();

        if(this.carGame.accessCarGame()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    carGame.computeResults(mRgba);
                }
            };
            thread.start();
        }

        return this.drawPredicts(mRgba, tiles);
    }

    protected Mat drawPredicts(Mat in, List<Tile> tiles){

        if(tiles.isEmpty()){
            return  in;
        }

        // Rotate original image by 90 degree to get portrait frame
        Mat out=new Mat();
        Core.flip(in.t(), out, 1);

        Iterator colorIterator = colors.iterator();

        for(Tile tile: tiles){

            if(!colorIterator.hasNext())
                colorIterator = colors.iterator();

            Scalar color = (Scalar)colorIterator.next();

            Imgproc.rectangle(out,
                    new Point(tile.getCarPart().getCenterX() - tile.getSize()/2f, tile.getCarPart().getCenterY() - tile.getSize()/2f),
                    new Point(tile.getCarPart().getCenterX() + tile.getSize()/2f, tile.getCarPart().getCenterY() + tile.getSize()/2f),
                    color, 2);

            Imgproc.putText(out, tile.getLabel().toString(),
                    new Point(tile.getCarPart().getCenterX() - tile.getSize()/2f, tile.getCarPart().getCenterY() + tile.getSize()/2),
                    Core.FONT_HERSHEY_SIMPLEX,
                    0.75f, color, 2);
        }
        // Rotate back by -90 degree
        Core.flip(out.t(), out, 0);

        return out;
    }

}