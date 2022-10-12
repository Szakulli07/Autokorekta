package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Tile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BiofuelCar extends Car{

    List<Label> neededLabels = Arrays.asList(
            Label.CHASSIS,
            Label.BODY,
            Label.ON_BOARD_COMPUTER,
            Label.ENGINE
    );

    public BiofuelCar(Tile startingTile){
        super(startingTile);
    }

    @Override
    public void addTile(Tile tile) {
        if(tile.getCarType() == Label.BIOFUEL
                && tile.getRotation() == this.tiles.get(0).getRotation()){
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

        if(this.checkDistance(Label.CHASSIS, Label.ENGINE) != 1){
            return  false;
        }

        if(this.checkDistance(Label.ENGINE, Label.BODY) != 1){
            return  false;
        }

        if(this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER) != 1){
            return  false;
        }

        return true;
    }

}
