package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.R;
import com.example.imagepro.Tile;

import org.opencv.core.Scalar;

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
    public String getName() {
        return "Bio";
    }

    @Override
    public Scalar getColor() {
        return new Scalar(232, 39, 177);
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

        int distanceAB;
        int distanceBA = 0;


        distanceAB = this.checkDistance(Label.CHASSIS, Label.ENGINE);
        distanceBA = this.checkDistance(Label.ENGINE, Label.CHASSIS);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){ return  false; }

        distanceAB = this.checkDistance(Label.ENGINE, Label.BODY);
        distanceBA = this.checkDistance(Label.BODY, Label.ENGINE);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){ return  false; }

        distanceAB = this.checkDistance(Label.BODY, Label.ON_BOARD_COMPUTER);
        distanceBA = this.checkDistance(Label.ON_BOARD_COMPUTER, Label.BODY);
        if( (distanceAB < 0 || distanceAB > 2) && (distanceBA < 0 || distanceBA >2) ){ return  false; }

        return true;
    }

}
