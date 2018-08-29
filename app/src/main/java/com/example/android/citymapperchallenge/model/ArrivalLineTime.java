package com.example.android.citymapperchallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ArrivalLineTime implements Parcelable{

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

    //parcellable methods
    public static final Creator CREATOR = new Creator() {
        @Override
        public ArrivalLineTime createFromParcel(Parcel parcel) {
            return new ArrivalLineTime(parcel);
        }

        @Override
        public ArrivalLineTime[] newArray(int i) {
            return new ArrivalLineTime[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mLineId);
        parcel.writeString(mLineName);
        parcel.writeLong(mTime);
    }

    private ArrivalLineTime(Parcel in) {
        mLineId = in.readString();
        mLineName = in.readString();
        mTime = in.readLong();
    }
}
