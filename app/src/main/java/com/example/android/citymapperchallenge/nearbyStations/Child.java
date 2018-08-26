
package com.example.android.citymapperchallenge.nearbyStations;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Child {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("additionalProperties")
    private List<AdditionalProperty> mAdditionalProperties;
    @SerializedName("children")
    private List<Child> mChildren;
    @SerializedName("commonName")
    private String mCommonName;
    @SerializedName("icsCode")
    private String mIcsCode;
    @SerializedName("id")
    private String mId;
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lineGroup")
    private List<LineGroup> mLineGroup;
    @SerializedName("lineModeGroups")
    private List<LineModeGroup> mLineModeGroups;
    @SerializedName("lines")
    private List<Line> mLines;
    @SerializedName("lon")
    private Double mLon;
    @SerializedName("modes")
    private List<Object> mModes;
    @SerializedName("naptanId")
    private String mNaptanId;
    @SerializedName("placeType")
    private String mPlaceType;
    @SerializedName("stationNaptan")
    private String mStationNaptan;
    @SerializedName("status")
    private Boolean mStatus;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public List<AdditionalProperty> getAdditionalProperties() {
        return mAdditionalProperties;
    }

    public void setAdditionalProperties(List<AdditionalProperty> additionalProperties) {
        mAdditionalProperties = additionalProperties;
    }

    public List<Child> getChildren() {
        return mChildren;
    }

    public void setChildren(List<Child> children) {
        mChildren = children;
    }

    public String getCommonName() {
        return mCommonName;
    }

    public void setCommonName(String commonName) {
        mCommonName = commonName;
    }

    public String getIcsCode() {
        return mIcsCode;
    }

    public void setIcsCode(String icsCode) {
        mIcsCode = icsCode;
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

    public List<LineGroup> getLineGroup() {
        return mLineGroup;
    }

    public void setLineGroup(List<LineGroup> lineGroup) {
        mLineGroup = lineGroup;
    }

    public List<LineModeGroup> getLineModeGroups() {
        return mLineModeGroups;
    }

    public void setLineModeGroups(List<LineModeGroup> lineModeGroups) {
        mLineModeGroups = lineModeGroups;
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

    public List<Object> getModes() {
        return mModes;
    }

    public void setModes(List<Object> modes) {
        mModes = modes;
    }

    public String getNaptanId() {
        return mNaptanId;
    }

    public void setNaptanId(String naptanId) {
        mNaptanId = naptanId;
    }

    public String getPlaceType() {
        return mPlaceType;
    }

    public void setPlaceType(String placeType) {
        mPlaceType = placeType;
    }

    public String getStationNaptan() {
        return mStationNaptan;
    }

    public void setStationNaptan(String stationNaptan) {
        mStationNaptan = stationNaptan;
    }

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

}
