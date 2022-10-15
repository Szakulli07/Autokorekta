package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Rotation;
import com.example.imagepro.Tile;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class Car {

    public Rotation getRotation(){
        return this.getTiles().get(0).getRotation();
    }

    protected int checkDistance(Label firstLabel, Label secondLabel){
        Tile firstTile = null;

        List<Tile> tiles = this.getTiles();

        for(Tile tile: tiles){
            if(tile.getCarPart().getLabel() == firstLabel){
                firstTile = tile;
            }
        }

        if(firstTile == null){
            return -1;
        }

        Map<Tile, Boolean> seenTiles = new HashMap<>();
        for (Tile tile: tiles) {
            seenTiles.put(tile, Boolean.FALSE);
        }

        Map<Tile, Integer> distance = new HashMap<>();
        for (Tile tile: tiles) {
            distance.put(tile, 0);
        }

        Queue<Tile> carQueue = new LinkedList<>(Collections.singletonList(firstTile));

        while(!carQueue.isEmpty()){
            Tile tile = carQueue.poll();
            seenTiles.put(tile, Boolean.TRUE);

            for (Tile neighbourTile: tile.getNeighbours()) {

                if(neighbourTile.getCarPart().getLabel() == secondLabel){
                    return distance.get(tile)+1;
                }

                if(seenTiles.containsKey(neighbourTile) && !seenTiles.get(neighbourTile) &&
                    Label.labelGroup(neighbourTile.getCarPart().getLabel()) == Label.labelGroup(tile.getCarPart().getLabel())){
                    seenTiles.replace(neighbourTile, Boolean.TRUE);
                    distance.replace(neighbourTile, distance.get(tile)+1);
                    carQueue.add(neighbourTile);
                }
            }
        }

        return -1;
    }

    public boolean isValid(){

        List<Tile> tiles = this.getTiles();
        List<Label> neededLabels = this.getNeededLabels();

        if(neededLabels.size() != tiles.size()){
            return false;
        }

        List<Label> tileLabels = tiles.stream()
                .map(tile -> tile.getCarPart().getLabel())
                .collect(Collectors.toList());

        return tileLabels.containsAll(neededLabels);
    }

    public Mat draw(Mat in){
        Mat out=new Mat();

        Core.flip(in.t(), out, 1);

        String rotation = "";

        switch (this.getRotation()){
            case DOWN:
                rotation = "D";
                break;
            case UPPER:
                rotation = "U";
                break;
            case LEFT:
                rotation = "L";
                break;
            case RIGHT:
                rotation = "R";
                break;
            case ERROR:
                rotation = "E";
                break;
        }

        Scalar color = new Scalar(255, 0, 0);

        if(this.isValid()){
            color = this.getColor();

        }

        List<Tile> tiles = this.getTiles();

        for (Tile tile: tiles) {
            Imgproc.putText(out, rotation + "_" + this.getName(),
                    new Point(tile.getCarPart().getCenterX() - tile.getSize()/4f, tile.getCarPart().getCenterY()),
                    Core.FONT_HERSHEY_SIMPLEX,
                    0.75f, color, 2);
        }

        Core.flip(out.t(), out, 0);

        return out;
    }

    public abstract void addTile(Tile tile);

    public abstract Label getType();

    public abstract String getName();

    public abstract Scalar getColor();

    public abstract List<Tile> getTiles();

    public abstract List<Label> getNeededLabels();

}
