package com.example.android.citymapperchallenge.model;

import java.util.ArrayList;

public class StationArrivals {

    private String mStation;
    private String mNaptanId;
    private double mDistance;
    private ArrayList<ArrivalLineTime> mArrivals;

    public StationArrivals(){};

    public StationArrivals(String station, String naptanId, double distance,
                           ArrayList<ArrivalLineTime> arrivals){
        mStation = station;
        mNaptanId = naptanId;
        mDistance = distance;
        mArrivals = arrivals;
    }

    public String getStation(){
        return mStation;
    }

    public String getNaptanId(){
        return mNaptanId;
    }

    public double getDistance(){
        return mDistance;
    }

    public ArrayList<ArrivalLineTime> getArrivals() {
        return mArrivals;
    }

    public void setStation(String station){
        mStation = station;
    }

    public void setNaptanId(String naptanId){
        mNaptanId = naptanId;
    }

    public void setDistance(double distance){ mDistance = distance;}

    public void setArrivals(ArrayList<ArrivalLineTime> arrivals){
        mArrivals = arrivals;
    }
}
