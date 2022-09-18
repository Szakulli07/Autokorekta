package com.example.imagepro;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Comparator;
import java.util.List;


public class Prediction {
    private final float centerX;
    private float leftX;
    private float rightX;

    private final float centerY;
    private float downY;
    private float upperY;

    private final float height;
    private final float width;

    private int label;
    private float labelScore;
    private boolean isSmallLabel;

    public Prediction(float height, float width, float[] predict, List<Integer> smallLabelList){
        this.centerX = predict[0];
        this.centerY = predict[1];
        this.width = predict[2];
        this.height = predict[3];
        this.labelScore = predict[4];

        this.setHighestLabel(predict);
        this.isSmallLabel = smallLabelList.contains(this.label);
        this.setCorners(height, width);

    }

    private void setHighestLabel(float[] predict){
        int argMaxClass = -1;
        float maxClassScore = -1;
        for (int i = 5; i < predict.length; i++) {
            if (Float.compare(predict[i], maxClassScore)>0) {
                maxClassScore = predict[i];
                argMaxClass = i;
            }
        }
        this.label = argMaxClass -5;
    }

    private void setCorners(float height, float width){
        this.leftX = (this.centerX - this.width / 2) * width;
        this.downY = (this.centerY - this.height / 2) * height;
        this.rightX = (this.centerX + this.width / 2) * width;
        this.upperY = (this.centerY + this.height / 2) * height;
    }

    public float getArea(){
        return (this.rightX-this.leftX)*(this.upperY-this.downY);
    }

    public float calculateIoU(Prediction pr){

        // Find intersection box
        float minLeftX = min(this.leftX, pr.leftX);
        float maxRightX = max(this.rightX, pr.rightX);
        float minDownY = min(this.downY, pr.downY);
        float maxUpperY = max(this.upperY, pr.upperY);

        float intersectionWidth = max(0, maxRightX - minLeftX);
        float intersectionHight = max(0, maxUpperY - minDownY);

        float intersectionArea = intersectionWidth*intersectionHight;

        float unionArea = this.getArea() + pr.getArea() - intersectionArea;

        float iou = intersectionArea / unionArea;

        return iou;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getLeftX() {
        return leftX;
    }

    public float getRightX() {
        return rightX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getDownY() {
        return downY;
    }

    public float getUpperY() {
        return upperY;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public int getLabel() {
        return label;
    }

    public float getLabelScore() {
        return labelScore;
    }

    public boolean isSmallLabel() { return isSmallLabel; }

}
