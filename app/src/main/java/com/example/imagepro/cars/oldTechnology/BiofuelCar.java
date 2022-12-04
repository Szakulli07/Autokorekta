package com.example.imagepro.cars.oldTechnology;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;
import com.example.imagepro.cars.Car;
import com.example.imagepro.cars.CarStatus;

import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BiofuelCar extends Car {

    List<Tile> tiles = new ArrayList<>();

    List<Label> neededLabels = Arrays.asList(
            Label.CHASSIS,
            Label.BODY,
            Label.ON_BOARD_COMPUTER,
            Label.ENGINE
    );

    private Boolean isValid = null;

    private CarStatus carStatus = CarStatus.ERROR;

    public BiofuelCar(Tile startingTile){
        this.tiles.add(startingTile);
    }

    @Override
    public Label getType(){ return Label.BIOFUEL;}

    @Override
    public CarStatus getStatus() {
        return carStatus;
    }

    @Override
    public String getName() {
        return "BIO";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(232, 39, 39);
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
            if(tile.getLabel() != Label.BIOFUEL
                || tile.isNewTechnology()){
                this.carStatus = CarStatus.TYPE;
                this.isValid = false;
                return false;
            }
        }

        int distanceAB;
        int distanceBA;


        distanceAB = this.checkDistance(Label.CHASSIS, Label.ENGINE);
        distanceBA = this.checkDistance(Label.ENGINE, Label.CHASSIS);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            this.isValid = false;
            return  false;
        }

        distanceAB = this.checkDistance(Label.ENGINE, Label.BODY);
        distanceBA = this.checkDistance(Label.BODY, Label.ENGINE);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){
            this.isValid = false;
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            return  false;
        }

        distanceAB = this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER);
        distanceBA = this.checkDistance(Label.ON_BOARD_COMPUTER, Label.BODY);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){
            this.isValid = false;
            this.carStatus = CarStatus.MAIN_CONNECTIONS;
            return  false;
        }

        this.carStatus = CarStatus.VALID;
        this.isValid = true;
        return true;
    }

}
