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

public class GraphActivity extends DetectionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = findViewById(R.id.title_text);
        title.setText(R.string.graph);
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

            for(Tile neighbourTile: tile.getNeighbours()){

                Imgproc.line(out,
                        new Point(tile.getCarPart().getCenterX(), tile.getCarPart().getCenterY()),
                        new Point((neighbourTile.getCarPart().getCenterX()+tile.getCarPart().getCenterX())/2,
                                (neighbourTile.getCarPart().getCenterY()+tile.getCarPart().getCenterY())/2),
                        color, 2);
            }
        }
        // Rotate back by -90 degree
        Core.flip(out.t(), out, 0);

        return out;
    }

}