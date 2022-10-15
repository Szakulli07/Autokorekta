package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;

import org.opencv.core.Scalar;

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

    @Override
    public Label getType(){ return Label.BLANK;}

    @Override
    public String getName() {
        return "Err";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(255, 255, 255);
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
}
