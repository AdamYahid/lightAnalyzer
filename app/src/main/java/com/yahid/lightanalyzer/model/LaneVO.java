package com.yahid.lightanalyzer.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Adam on 9/25/2015.
 */
public class LaneVO {
    private int length;
    private double[] data;

    public LaneVO() {

    }

    public LaneVO(int length) {
        this.length = length;
        data = new double[length];
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength()
    {
        return this.length;
    }

    public double getPointValue(int index) {
        return data[index];
    }

    public void setPointValue(int index,double value) {
        this.data[index] = value;
    }

    public void readString(String laneData) {
        String[] laneValues = laneData.split(",");
        double[] data = new double[laneValues.length];
        for (int i=0; i < laneValues.length; i++) {
            data[i] = Double.parseDouble(laneValues[i]);
        }
        this.length = data.length;
        this.data = data;
    }

    public String toString() {
        String res = "";
        for (int i=0; i < this.length; i++) {
            res += this.data[i];
            if (i!=this.length-1) {
                res+=",";
            }
        }
        return res;
    }
}
