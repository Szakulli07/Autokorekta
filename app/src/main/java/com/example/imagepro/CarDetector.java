package com.example.imagepro;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class CarDetector {

    private List<Tile> cars = new ArrayList<>();

    public List<Tile> getCars() {
        return cars;
    }

    public void setCars(List<Tile> cars) {
        this.cars = cars;
    }

    public void detectCars(List<Tile> tiles){
        Map<Boolean, List<Tile>> partitions = tiles.stream()
                .collect(Collectors.partitioningBy(tile -> tile.getCarPart().getLabel() == Label.CHASSIS));

        if(partitions.get(true) != null){
            Queue<Tile> startTiles = new LinkedList<>(partitions.get(true));
            List<Tile> otherTiles = partitions.get(false);

            Map<Tile, Boolean> seenPoints = new HashMap<>();
            for (Tile tile: tiles) {
                seenPoints.put(tile, Boolean.FALSE);
            }

            this.carBFS(startTiles, seenPoints);
        }

        this.setCars(tiles);
    }

    private void carBFS(Queue<Tile> startPoints, Map<Tile, Boolean> seenPoints){
        Log.d("CAR", "START BFS");

        while (!startPoints.isEmpty()){
            Queue<Tile> carQueue = new LinkedList<>(Collections.singleton(startPoints.poll()));

            while(!carQueue.isEmpty()){
                Tile tile = carQueue.poll();
                Log.d("CAR", tile.getCarPart().getLabel().toString());

                seenPoints.put(tile, Boolean.TRUE);

                for (Tile neighbourTile: tile.getNeighbours()) {

                    if(!seenPoints.get(neighbourTile)
                            && neighbourTile.getRotation() == tile.getRotation()
                            && neighbourTile.getCarType() == tile.getCarType()){
                        seenPoints.put(neighbourTile, Boolean.TRUE);
                        carQueue.add(neighbourTile);
                    }
                }
            }
        }
    }
}
