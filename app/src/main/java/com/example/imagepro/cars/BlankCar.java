package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlankCar extends Car{

    List<Tile> tiles = new ArrayList<>();

    List<Label> neededLabels = Arrays.asList();

    private Boolean isValid = null;

    public BlankCar(Tile startingTile){
        this.tiles.add(startingTile);
    }

    private CarStatus carStatus = CarStatus.ERROR;

    @Override
    public CarStatus getStatus() {
        return carStatus;
    }

    @Override
    public Label getType(){ return Label.BLANK;}

    @Override
    public String getName() {
        return "ERR";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(256, 256, 256);
    }

    @Override
    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public List<Label> getNeededLabels() {
        return neededLabels;
    }

    @Override
    public void addTile(Tile tile) {
        if(tile.getRotation() == this.tiles.get(0).getRotation()){
            this.tiles.add(tile);
        }
    }

    @Override
    public  boolean isValid(){
        return false;
    }

    @Override
    public Mat draw(Mat in){
        Mat out=new Mat();

        Core.flip(in.t(), out, 1);

        Scalar color = this.getColor();

        List<Tile> tiles = this.getTiles();

        for (Tile tile: tiles) {
            Imgproc.putText(out, "XXX",
                    new Point(tile.getCarPart().getLeftX(),
                            tile.getCarPart().getUpperY()),
                    Core.FONT_HERSHEY_SIMPLEX,
                    2f, new Scalar(256, 256, 256), 2);
        }

        Core.flip(out.t(), out, 0);

        return out;
    }
}
