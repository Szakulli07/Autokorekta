package com.example.imagepro;

import android.content.res.AssetManager;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;

public class CarGame {
    private final ObjectDetector  objectDetector;
    private final TileDetector tileDetector = new TileDetector();
    private boolean isDetecting = false;


    public CarGame(AssetManager assetManager) throws IOException {
            objectDetector=new ObjectDetector(assetManager,
                    "cars.tflite",
                    "labelmap.txt",
                    "smalllabels.txt",
                    "teamlabels.txt",
                    640);
    }

    private synchronized void changeDetecting(boolean isDetecting){
        this.isDetecting = isDetecting;
    }

    public synchronized boolean accessCarGame(){
        if(this.isDetecting){
            return  false;
        }else{
            this.changeDetecting(true);
            return true;
        }
    }

    public void computeResults(Mat frame){
        this.objectDetector.recognizeImage(frame);
        List<Prediction> predictions = this.objectDetector.getPredicts();
        this.tileDetector.detectTiles(predictions);
        this.changeDetecting(false);
    }

    public List<Tile> getResults(){
        return tileDetector.getTiles();
    }
}

