package com.example.imagepro;

import android.service.autofill.FillEventHistory;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private boolean isBiofuel;
    private boolean isElectric;
    private boolean isHybrid;
    private boolean isSolar;

    public float size=0;

    private final List<Prediction> carTypes;
    private List<Tile> neighbours = new ArrayList<>();
    private final Prediction carPart;
    private Rotation rotation;

    public Tile(Prediction carPart, List<Prediction> carTypes) {
        this.carPart = carPart;
        this.carTypes = carTypes;
        this.setRotation();
        this.setSize();
    }

    public Prediction getCarPart() {
        return this.carPart;
    }

    public double[] getCoords(){ return this.carPart.getCoords(); }

    public Rotation getRotation(){
        return this.rotation;
    }


    private void setRotation(){
        if(carTypes == null){
            this.rotation = Rotation.ERROR;
            return;
        }
        switch (this.carTypes.size()){
            case 1:
                this.rotation = this.getRotationFromOne();
                break;
            case 2:
                this.rotation = this.getRotationFromTwo();
                break;
            case 3:
                this.rotation = this.getRotationFromThree();
                break;
            default:
                this.rotation = Rotation.ERROR;
        }
    }

    private Rotation getRotationFromOne() {
        Prediction carType = this.carTypes.get(0);

        float diffX =  this.carPart.getCenterX() - carType.getCenterX();
        float diffY = this.carPart.getCenterY() - carType.getCenterY();

        if (diffX > 0 && diffY > 0) {
            return Rotation.LEFT;
        }else if (diffX > 0 && diffY <= 0){
            return Rotation.DOWN;
        }else if (diffX < 0 && diffY > 0){
            return Rotation.UPPER;
        }else{
            return Rotation.RIGHT;
        }
    }

    private Rotation getRotationFromTwo() {
        Prediction carType = this.carTypes.get(0);
        Prediction carType2 = this.carTypes.get(1);

        float diffX =  this.carPart.getCenterX() - carType.getCenterX();
        float diffY = this.carPart.getCenterY() - carType.getCenterY();
        float diffX2 =  this.carPart.getCenterX() - carType2.getCenterX();
        float diffY2 = this.carPart.getCenterY() - carType2.getCenterY();

        if (diffX >= 0 && diffX2 >= 0){
            return Rotation.LEFT;
        }else if (diffX <=0 && diffX2 <= 0){
            return Rotation.RIGHT;
        }else if (diffY >=0 && diffY2 >= 0){
            return Rotation.UPPER;
        }else{
            return Rotation.DOWN;
        }
    }

    private Rotation getRotationFromThree() {
        switch (this.carPart.getLabel()){
            case ON_BOARD_COMPUTER:
                return this.getRotationFromThreeLeft();
            case ENGINE:

                return this.getRotationFromThreeRight();
            default:
                return Rotation.ERROR;
        }
    }

    private Rotation getRotationFromThreeLeft() {
        Prediction carType = this.carTypes.get(0);
        Prediction carType2 = this.carTypes.get(1);
        Prediction carType3 = this.carTypes.get(2);

        float diffX =  this.carPart.getCenterX() - carType.getCenterX();
        float diffY = this.carPart.getCenterY() - carType.getCenterY();
        float diffX2 =  this.carPart.getCenterX() - carType2.getCenterX();
        float diffY2 = this.carPart.getCenterY() - carType2.getCenterY();
        float diffX3 =  this.carPart.getCenterX() - carType3.getCenterX();
        float diffY3 = this.carPart.getCenterY() - carType3.getCenterY();

        if(!((diffX>0 && diffY>0) || (diffX2>0 && diffY2>0) || (diffX3>0 && diffY3>0))){
            return Rotation.RIGHT;
        }else if (!((diffX>0 && diffY<=0) || (diffX2>0 && diffY2<=0) || (diffX3>0 && diffY3<=0))){
            return Rotation.UPPER;
        }else if (!((diffX<0 && diffY>0) || (diffX2<0 && diffY2>0) || (diffX3<0 && diffY3>0))){
            return Rotation.DOWN;
        }else{
            return Rotation.LEFT;
        }
    }

    private Rotation getRotationFromThreeRight() {
        Prediction carType = this.carTypes.get(0);
        Prediction carType2 = this.carTypes.get(1);
        Prediction carType3 = this.carTypes.get(2);

        float diffX =  this.carPart.getCenterX() - carType.getCenterX();
        float diffY = this.carPart.getCenterY() - carType.getCenterY();
        float diffX2 =  this.carPart.getCenterX() - carType2.getCenterX();
        float diffY2 = this.carPart.getCenterY() - carType2.getCenterY();
        float diffX3 =  this.carPart.getCenterX() - carType3.getCenterX();
        float diffY3 = this.carPart.getCenterY() - carType3.getCenterY();

        if(!((diffX>0 && diffY>0) || (diffX2>0 && diffY2>0) || (diffX3>0 && diffY3>0))){
            return Rotation.DOWN;
        }else if (!((diffX>0 && diffY<=0) || (diffX2>0 && diffY2<=0) || (diffX3>0 && diffY3<=0))){
            return Rotation.RIGHT;
        }else if (!((diffX<0 && diffY>0) || (diffX2<0 && diffY2>0) || (diffX3<0 && diffY3>0))){
            return Rotation.LEFT;
        }else{
            return Rotation.UPPER;
        }
    }

    private void setSize(){
        if(this.carTypes == null || this.carTypes.size() == 0){
            return;
        }
        float distance =  this.carPart.getDistance(this.carTypes.get(0));
        this.size = distance * 2f;
    }

    private float getDistance(Tile tile){
        return this.carPart.getDistance(tile.getCarPart());
    }

    public void addNeighbour(Tile neighbour){
        float distance = this.getDistance(neighbour);

        if(distance < this.size*0.1f){ return;}

        if(distance > this.size*1.2f){ return;}

        this.neighbours.add(neighbour);
    }

    public List<Tile> getNeighbours(){
        return this.neighbours;
    }
}
