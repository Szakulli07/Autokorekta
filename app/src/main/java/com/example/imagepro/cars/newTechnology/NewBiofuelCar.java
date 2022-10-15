package com.example.imagepro.cars.newTechnology;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;
import com.example.imagepro.cars.Car;

import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewBiofuelCar extends Car {

    List<Tile> tiles = new ArrayList<>();

    List<Label> neededLabels = Arrays.asList(
            Label.CHASSIS,
            Label.BODY,
            Label.ON_BOARD_COMPUTER,
            Label.ENGINE
    );

    public NewBiofuelCar(Tile startingTile){
        this.tiles.add(startingTile);
    }


    @Override
    public String getName() {
        return "BIO";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(232, 39, 177);
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
        if(neededLabels.size() != tiles.size()){
            return false;
        }

        List<Label> tileLabels = tiles.stream()
                .map(tile -> tile.getCarPart().getLabel())
                .collect(Collectors.toList());

        if(!tileLabels.containsAll(neededLabels)){
            return false;
        }

        for (Tile tile: tiles) {
            if(!(tile.getLabel() == Label.BIOFUEL || tile.getLabel() == Label.BIO_HYBRID)
                || !tile.isNewTechnology()){
                return false;
            }
        }

        int distance;

        distance = this.checkDistance(Label.CHASSIS, Label.ENGINE);
        if( distance != 1 ){ return  false; }

        distance = this.checkDistance(Label.ENGINE, Label.BODY);
        if( distance != 1 ){ return  false; }

        distance = this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER);
        if( distance != 1 ){ return  false; }

        return true;
    }

}
