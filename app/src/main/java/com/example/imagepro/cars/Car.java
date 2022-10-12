package com.example.imagepro.cars;

import com.example.imagepro.Label;
import com.example.imagepro.Rotation;
import com.example.imagepro.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class Car {

    List<Tile> tiles = new ArrayList<>();
    List<Label> neededLabels = new ArrayList<>();

    public Car(Tile startingTile){
        this.tiles.add(startingTile);
    }

    public Rotation getRotation(){
        return this.tiles.get(0).getRotation();
    }

    protected int checkDistance(Label firstLabel, Label secondLabel){
        Tile firstTile = null;

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

                if(!seenTiles.get(neighbourTile)){
                    seenTiles.replace(neighbourTile, Boolean.TRUE);
                    distance.replace(neighbourTile, distance.get(tile)+1);
                    carQueue.add(neighbourTile);
                }
            }
        }

        return -1;
    }

    public boolean isValid(){
        if(neededLabels.size() != tiles.size()){
            return false;
        }

        List<Label> tileLabels = tiles.stream()
                .map(tile -> tile.getCarPart().getLabel())
                .collect(Collectors.toList());

        return tileLabels.containsAll(neededLabels);
    }


    public abstract void addTile(Tile tile);

}
