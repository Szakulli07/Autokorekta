package com.example.imagepro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileDetector {

    public List<Tile> detectTiles(List<Prediction> predictions){
        Map<Boolean, List<Prediction>> partitions = predictions.stream()
                .collect(Collectors.partitioningBy(p -> (p.isSmallLabel())));
        List<Prediction> smallPredictions = partitions.get(true);
        List<Prediction> bigPredictions = partitions.get(false);
        List<Tile> tiles = bigPredictions.stream()
                .map(p -> new Tile(p))
                .collect(Collectors.toList());
        return tiles;
    }
}
