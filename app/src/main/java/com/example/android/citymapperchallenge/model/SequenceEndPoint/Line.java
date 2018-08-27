
package com.example.android.citymapperchallenge.model.SequenceEndPoint;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Line {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("crowding")
    private Crowding mCrowding;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("routeType")
    private String mRouteType;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("type")
    private String mType;
    @SerializedName("uri")
    private String mUri;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public Crowding getCrowding() {
        return mCrowding;
    }

    public void setCrowding(Crowding crowding) {
        mCrowding = crowding;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRouteType() {
        return mRouteType;
    }

    public void setRouteType(String routeType) {
        mRouteType = routeType;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

}
