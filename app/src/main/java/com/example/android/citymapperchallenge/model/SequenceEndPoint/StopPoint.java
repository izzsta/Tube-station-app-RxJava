
package com.example.android.citymapperchallenge.model.SequenceEndPoint;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class StopPoint {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("icsId")
    private String mIcsId;
    @SerializedName("id")
    private String mId;
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lines")
    private List<Line> mLines;
    @SerializedName("lon")
    private Double mLon;
    @SerializedName("modes")
    private List<String> mModes;
    @SerializedName("name")
    private String mName;
    @SerializedName("parentId")
    private String mParentId;
    @SerializedName("stationId")
    private String mStationId;
    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("stopType")
    private String mStopType;
    @SerializedName("topMostParentId")
    private String mTopMostParentId;
    @SerializedName("zone")
    private String mZone;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public String getIcsId() {
        return mIcsId;
    }

    public void setIcsId(String icsId) {
        mIcsId = icsId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public List<Line> getLines() {
        return mLines;
    }

    public void setLines(List<Line> lines) {
        mLines = lines;
    }

    public Double getLon() {
        return mLon;
    }

    public void setLon(Double lon) {
        mLon = lon;
    }

    public List<String> getModes() {
        return mModes;
    }

    public void setModes(List<String> modes) {
        mModes = modes;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getParentId() {
        return mParentId;
    }

    public void setParentId(String parentId) {
        mParentId = parentId;
    }

    public String getStationId() {
        return mStationId;
    }

    public void setStationId(String stationId) {
        mStationId = stationId;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public String getStopType() {
        return mStopType;
    }

    public void setStopType(String stopType) {
        mStopType = stopType;
    }

    public String getTopMostParentId() {
        return mTopMostParentId;
    }

    public void setTopMostParentId(String topMostParentId) {
        mTopMostParentId = topMostParentId;
    }

    public String getZone() {
        return mZone;
    }

    public void setZone(String zone) {
        mZone = zone;
    }

}
