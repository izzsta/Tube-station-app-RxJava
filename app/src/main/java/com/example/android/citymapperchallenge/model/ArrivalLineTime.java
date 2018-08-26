package com.example.android.citymapperchallenge.model;

public class ArrivalLineTime {

    private String mLineId;
    private String mLineName;
    private long mTime;

    public ArrivalLineTime(String lineId, String lineName, long time){
        mLineId = lineId;
        mLineName = lineName;
        mTime = time;
    }

    public String getLineId(){
        return mLineId;
    }

    public String getLineName(){
        return mLineName;
    }

    public long getTime(){
        return mTime;
    }

    public void setLineId(String lineId){
        mLineId = lineId;
    }

    public void setLineName(String lineName){
        mLineName = lineName;
    }

    public void setTime(long time){
        mTime = time;
    }
}
