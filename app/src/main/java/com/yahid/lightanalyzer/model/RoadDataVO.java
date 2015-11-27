package com.yahid.lightanalyzer.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 9/19/2015.
 */
public class RoadDataVO {

    private String streetName;
    private int laneCount;
    private int laneLength;
    private int poleHeight;
    private LaneVO[] roadData;

    public static final String STREET_NAME_TAG = "streetName";
    public static final String LANE_COUNT_TAG = "laneCount";
    public static final String LANE_LENGTH_TAG = "laneLength";
    public static final String POLE_HEIGHT_TAG = "poleHeight";
    public static final String LIGHT_DATA_TAG = "lightData";

    public RoadDataVO()
    {

    }

    public RoadDataVO(String name, int laneCount, int laneLength, int poleHeight) {
        this.streetName = name;
        this.laneCount = laneCount;
        this.laneLength = laneLength;
        this.poleHeight = poleHeight;
        this.roadData = new LaneVO[laneCount];
        for (int i = 0; i < laneCount; i++) {
            this.roadData[i] = new LaneVO(laneLength);
        }
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getLaneCount() {
        return laneCount;
    }

    public void setLaneCount(int laneCount) {
        this.laneCount = laneCount;
    }

    public int getPoleHeight() {
        return poleHeight;
    }

    public void setPoleHeight(int poleHeight) {
        this.poleHeight = poleHeight;
    }

    public int getLaneLength() {
        return laneLength;
    }

    public void setLaneLength(int laneLength) {
        this.laneLength = laneLength;
    }

    public void setDataPoint(int laneIndex, int indexInLane, double value) {
        this.roadData[laneIndex].setPointValue(indexInLane, value);
    }

    public double getDataPoint(int laneIndex, int indexInLane) {
        return this.roadData[laneIndex].getPointValue(indexInLane);
    }

    public String writeJSON() {
        JSONObject retAsObj = new JSONObject();
        try {

            String ret = "";
            retAsObj.put(STREET_NAME_TAG, this.streetName);
            retAsObj.put(LANE_COUNT_TAG, this.laneCount);
            retAsObj.put(LANE_LENGTH_TAG, this.laneLength);
            retAsObj.put(POLE_HEIGHT_TAG, this.poleHeight);
            JSONObject lightDataObj = new JSONObject();
            for (int i = 0; i < laneCount; i++) {
                lightDataObj.put(String.valueOf(i), roadData[i].toString());
            }
            retAsObj.put(LIGHT_DATA_TAG, lightDataObj);

        } catch (JSONException ex) {
        }
        return retAsObj.toString();
    }

    public void readJSON(JSONObject json) {
        try {
            this.streetName = json.getString(STREET_NAME_TAG);
        }
        catch (JSONException ex) {}
    }
}
