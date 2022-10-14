package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;

import org.opencv.core.Scalar;

public class BlankCar extends Car{

    public BlankCar(Tile startingTile){
        super(startingTile);
    }

    @Override
    public String getName() {
        return "Err";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(255, 255, 255);
    }

    @Override
    public void addTile(Tile tile) {
        if(tile.getCarType() == Label.BLANK
                && tile.getRotation() == this.tiles.get(0).getRotation()){
            this.tiles.add(tile);
        }
    }

    @Override
    public  boolean isValid(){
        return false;
    }
}
