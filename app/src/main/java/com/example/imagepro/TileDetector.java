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

        List<Tile> result = createTiles(smallPredictions, bigPredictions);
        this.setTiles(result);
    }

    public List<Tile> createTiles(List<Prediction> smallPredictions, List<Prediction> bigPredictions){
        KDTree kdTree = this.createKDTree(bigPredictions);
        List<Tile> tiles = this.combinePartsWithTypes(smallPredictions, bigPredictions, kdTree);
        return this.createGraph(tiles, kdTree);
    }

    private KDTree createKDTree(List<Prediction> predictions){
        KDTree kdTree = new KDTree(2);
        for(int tileId=0; tileId<predictions.size(); tileId++){
            kdTree.insert(predictions.get(tileId).getCoords(), tileId);
        }
        return kdTree;
    }

    private List<Tile> combinePartsWithTypes(List<Prediction> smallPredictions, List<Prediction> bigPredictions, KDTree kdTree){
        List<Prediction>[] tilesSmallParts = new List[bigPredictions.size()];
        for(int tileId=0; tileId<bigPredictions.size(); tileId++) {
            tilesSmallParts[tileId] = new ArrayList<>();
        }

        if(bigPredictions.size() > 0){
            for(Prediction prediction: smallPredictions){
                int tileId = (int)kdTree.nearest(prediction.getCoords());
                tilesSmallParts[tileId].add(prediction);
            }
        }

        List<Tile> tiles = new ArrayList<>();
        for(int tileId=0; tileId<bigPredictions.size(); tileId++){
            tiles.add(new Tile(bigPredictions.get(tileId), tilesSmallParts[tileId]));
        }

        return tiles;
    }

    private List<Tile> createGraph(List<Tile> tiles, KDTree kdTree){
        int maxNeighbours = Math.min(5, tiles.size());
        for(Tile tile: tiles){
            Object[] neighbours = kdTree.nearest(tile.getCoords(), maxNeighbours);
            for(Object neighbour: neighbours){
                tile.addNeighbour(tiles.get((int)neighbour));
            }
        }
        return tiles;
    }
}
