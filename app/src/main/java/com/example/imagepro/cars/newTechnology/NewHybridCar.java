package com.example.imagepro.cars.newTechnology;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;
import com.example.imagepro.cars.Car;
import com.example.imagepro.cars.CarStatus;

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

    private CarStatus carStatus = CarStatus.ERROR;


    public NewHybridCar(Tile startingTile){
        this.tiles.add(startingTile);
    }

    @Override
    public CarStatus getStatus() {
        return carStatus;
    }

    @Override
    public Label getType(){ return Label.HYBRID;}

    @Override
    public String getName() {
        return "NHYB";
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
            this.carStatus = CarStatus.SIZE;
            this.isValid = false;
            return false;
        }

        List<Label> tileLabels = tiles.stream()
                .map(tile -> tile.getCarPart().getLabel())
                .collect(Collectors.toList());

        if(!tileLabels.containsAll(neededLabels)){
            this.carStatus = CarStatus.NOT_ENOUGH;
            this.isValid = false;
            return false;
        }

        for (Tile tile: tiles) {
            if(!(tile.getLabel() == Label.HYBRID || tile.getLabel() == Label.BIO_HYBRID)
                    || !tile.isNewTechnology()){
                this.carStatus = CarStatus.TYPE;
                this.isValid = false;
                return false;
            }
        }

        int distance;

        distance = this.checkDistance(Label.CHASSIS, Label.ENGINE);
        if( distance != 1 ){
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            this.isValid = false;
            return  false; }

        distance = this.checkDistance(Label.ENGINE, Label.BODY);
        if( distance != 1 ){
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            this.isValid = false;
            return  false; }

        distance = this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER);
        if( distance != 1 ){
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            this.isValid = false;
            return  false; }

        this.carStatus = CarStatus.VALID;
        this.isValid = true;
        return true;
    }

}
