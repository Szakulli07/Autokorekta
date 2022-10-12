package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;

public class BlankCar extends Car{

    public BlankCar(Tile startingTile){
        super(startingTile);
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
