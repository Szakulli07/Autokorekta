package com.example.imagepro;

import android.content.res.AssetManager;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;

public class CarGame {
    private final ObjectDetector  objectDetector;
    private final TileDetector tileDetector = new TileDetector();

    public CarGame(AssetManager assetManager) throws IOException {
            objectDetector=new ObjectDetector(assetManager,
                    "cars.tflite",
                    "labelmap.txt",
                    "smalllabels.txt",
                    "teamlabels.txt",
                    640);
    }

    public List<Prediction> getResults(Mat image){
        if (objectDetector.accessDetector()){
            Thread thread = new Thread() {
                @Override
                public void run(){
                    objectDetector.recognizeImage(image);
                }
            };
            thread.start();
        }

        return objectDetector.getPredicts();
    }
}
