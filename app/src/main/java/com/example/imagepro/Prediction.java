package com.example.imagepro;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Prediction {
    private final float centerX;
    private float leftX;
    private float rightX;

    private final float centerY;
    private float downY;
    private float upperY;

    private final float height;
    private final float width;

    private final Label label;
    private final float labelScore;
    private final boolean isSmallLabel;
    private final boolean isTeamLabel;

    public Prediction(float height, float width, float[] predict){
        this.centerX = predict[0] * width;
        this.centerY = predict[1] * height;
        this.width = predict[2] * width;
        this.height = predict[3] * height;
        this.labelScore = predict[4];

        this.label = setHighestLabel(predict);
        this.isSmallLabel = Label.isLabelSmall(this.label);
        this.isTeamLabel = Label.isLabelTeam(this.label);
        this.setCorners();

    }

    private Label setHighestLabel(float[] predict){
        int argMaxClass = -1;
        float maxClassScore = -1;
        for (int i = 5; i < predict.length; i++) {
            if (Float.compare(predict[i], maxClassScore)>0) {
                maxClassScore = predict[i];
                argMaxClass = i;
            }
        }
        return Label.getLabelFromId(argMaxClass-5);
    }

    private void setCorners(){
        this.leftX = (this.centerX - this.width / 2) ;
        this.downY = (this.centerY - this.height / 2);
        this.rightX = (this.centerX + this.width / 2);
        this.upperY = (this.centerY + this.height / 2);
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

        return intersectionArea / unionArea;
    }

    public float getDistance(Prediction prediction){
        float distance = 0;
        distance += Math.pow(this.getCenterX()- prediction.getCenterX(), 2);
        distance += Math.pow(this.getCenterY()- prediction.getCenterY(), 2);
        return (float) Math.sqrt(distance);
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

    public double[] getCoords(){
        return new double[]{this.getCenterX(), this.getCenterY()};
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Label getLabel() {
        return label;
    }

    public float getLabelScore() {
        return labelScore;
    }

    public boolean isSmallLabel() { return isSmallLabel; }

    public boolean isTeamLabel() { return isTeamLabel; }
}
