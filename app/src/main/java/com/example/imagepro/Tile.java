package com.example.imagepro;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private boolean isBiofuel;
    private boolean isElectric;
    private boolean isHybrid;
    private boolean isSolar;

    private List<Prediction> carTypes = new ArrayList<>();
    private Prediction carPart;

    public Tile(Prediction carPart) {
        this.carPart = carPart;
    }

    public void addCarType(Prediction carType){
        this.carTypes.add(carType);
    }

    public Prediction getCarPart() {
        return this.carPart;
    }
}
