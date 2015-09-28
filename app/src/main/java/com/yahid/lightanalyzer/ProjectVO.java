package com.yahid.lightanalyzer;

public class ProjectVO {
    private String streetName;
    private int width;
    private int height;
    private int poleHeight;
    private double[][] dataMatrix;

    public ProjectVO(String streetName,int height,int width,int poleHeight) {
        setStreetName(streetName);
        setWidth(width);
        setHeight(height);
        setPoleHeight(poleHeight);
        dataMatrix = new double[height][width];
    }

    public String getStreetName() {
        return streetName;
    }
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getPoleHeight() {
        return poleHeight;
    }
    public void setPoleHeight(int poleHeight) {
        this.poleHeight = poleHeight;
    }

    public void addValue(int row,int col,double value) {
        this.dataMatrix[row][col] = value;
    }

    public double getValue(int row,int col) {
        return this.dataMatrix[row][col];
    }


}
