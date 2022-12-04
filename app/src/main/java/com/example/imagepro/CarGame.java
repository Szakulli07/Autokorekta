package com.example.imagepro;

import android.content.res.AssetManager;

import com.example.imagepro.cars.Car;

import org.opencv.core.Mat;

import java.io.IOException;
import java.util.List;

public class CarGame {
    private final ObjectDetector  objectDetector;
    private final TileDetector tileDetector = new TileDetector();
    private final CarDetector carDetector = new CarDetector();
    private boolean isDetecting = false;


    public CarGame(AssetManager assetManager) throws IOException {
            objectDetector=new ObjectDetector(assetManager,
                    "cars.tflite",
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
        List<Tile> tiles = this.tileDetector.getTiles();

        this.carDetector.detectCars(tiles);
        this.changeDetecting(false);
    }

    public List<Tile> getTiles(){
        return tileDetector.getTiles();
    }
    public List<Prediction> getPredictions(){
        return objectDetector.getPredicts();
    }
    public List<Car> getCars() { return  carDetector.getCars(); }
    public List<Tile> getWrongTiles() { return carDetector.getWrongTiles(); }

}

