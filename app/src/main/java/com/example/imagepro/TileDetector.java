package com.example.imagepro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileDetector {

    private List<Tile> tiles = new ArrayList<>();

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public void detectTiles(List<Prediction> predictions){
        Map<Boolean, List<Prediction>> partitions = predictions.stream()
                .collect(Collectors.partitioningBy(p -> (p.isSmallLabel())));
        List<Prediction> smallPredictions = partitions.get(true);
        List<Prediction> bigPredictions = partitions.get(false);
        List<Tile> tiles = bigPredictions.stream()
                .map(p -> new Tile(p))
                .collect(Collectors.toList());
        List<Tile> result = findNearest(smallPredictions, tiles);
        this.setTiles(result);
    }

    public List<Tile> findNearest(List<Prediction> smallPreds, List<Tile> tiles){
        for(Prediction pred: smallPreds){
            int bestTileId = -1;
            float bestTileDistance = Float.MAX_VALUE;
            float predX = pred.getCenterX();
            float predY = pred.getCenterY();
            for(int id=0; id<tiles.size(); id++){
                float tileX = tiles.get(id).getCarPart().getCenterX();
                float tileY = tiles.get(id).getCarPart().getCenterY();
                float distance = (float)Math.pow(tileX - predX, 2) + (float)Math.pow(tileY - predY, 2);
                if (distance < bestTileDistance){
                    bestTileDistance = distance;
                    bestTileId = id;
                }
            }
            tiles.get(bestTileId).addCarType(pred);
        }

        return tiles;
    }
}
