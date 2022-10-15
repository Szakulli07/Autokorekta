package com.example.imagepro.cars.newTechnology;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;
import com.example.imagepro.cars.Car;

import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewHybridCar extends Car {

    List<Tile> tiles = new ArrayList<>();

    List<Label> neededLabels = Arrays.asList(
            Label.CHASSIS,
            Label.BODY,
            Label.ON_BOARD_COMPUTER,
            Label.ENGINE
    );

    private Boolean isValid = null;


    public NewHybridCar(Tile startingTile){
        this.tiles.add(startingTile);
    }

    @Override
    public Label getType(){ return Label.HYBRID;}

    @Override
    public String getName() {
        return "HYB";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(20, 149, 236);
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

        if(this.isValid != null){
            return this.isValid;
        }

        if(neededLabels.size() != tiles.size()){
            this.isValid = false;
            return false;
        }

        List<Label> tileLabels = tiles.stream()
                .map(tile -> tile.getCarPart().getLabel())
                .collect(Collectors.toList());

        if(!tileLabels.containsAll(neededLabels)){
            this.isValid = false;
            return false;
        }

        for (Tile tile: tiles) {
            if(!(tile.getLabel() == Label.HYBRID || tile.getLabel() == Label.BIO_HYBRID)
                    || !tile.isNewTechnology()){
                this.isValid = false;
                return false;
            }
        }

        int distance;

        distance = this.checkDistance(Label.CHASSIS, Label.ENGINE);
        if( distance != 1 ){
            this.isValid = false;
            return  false; }

        distance = this.checkDistance(Label.ENGINE, Label.BODY);
        if( distance != 1 ){
            this.isValid = false;
            return  false; }

        distance = this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER);
        if( distance != 1 ){
            this.isValid = false;
            return  false; }

        this.isValid = true;
        return true;
    }

}
