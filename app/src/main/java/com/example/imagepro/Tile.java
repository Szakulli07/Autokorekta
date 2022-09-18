package com.example.imagepro;

public class Tile {
    private boolean isBiofuel = false;
    private boolean isElectric = false;
    private boolean isHybrid = false;
    private boolean isSolar = false;
    private Prediction prediction;

    public Tile(Prediction prediction) {
        this.prediction = prediction;
    }

    public boolean isBiofuel() {
        return isBiofuel;
    }

    public void setBiofuel(boolean biofuel) {
        isBiofuel = biofuel;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public void setHybrid(boolean hybrid) {
        isHybrid = hybrid;
    }

    public boolean isSolar() {
        return isSolar;
    }

    public void setSolar(boolean solar) {
        isSolar = solar;
    }

    public Prediction getPrediction() {
        return prediction;
    }
}
