package com.example.imagepro;

import net.sf.javaml.core.kdtree.KDTree;

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
                .collect(Collectors.partitioningBy(Prediction::isSmallLabel));
        List<Prediction> smallPredictions = partitions.get(true);
        List<Prediction> bigPredictions = partitions.get(false);
        List<Tile> tiles = bigPredictions.stream()
                .map(Tile::new)
                .collect(Collectors.toList());
        List<Tile> result = findNearest(smallPredictions, tiles);
        this.setTiles(result);
    }

    public List<Tile> findNearest(List<Prediction> smallPreds, List<Tile> tiles){
        KDTree kdTree = new KDTree(2);
        for(int tileId=0; tileId<tiles.size(); tileId++){
            kdTree.insert(tiles.get(tileId).getCoords(), tileId);
        }

        for(Prediction pred: smallPreds){
            int tileId = (int)kdTree.nearest(pred.getCoords());
            tiles.get(tileId).addCarType(pred);
        }

        return tiles;
    }
}
