
package com.example.android.citymapperchallenge.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LineGroup {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("lineIdentifier")
    private List<String> mLineIdentifier;
    @SerializedName("naptanIdReference")
    private String mNaptanIdReference;
    @SerializedName("stationAtcoCode")
    private String mStationAtcoCode;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public List<String> getLineIdentifier() {
        return mLineIdentifier;
    }

    public void setLineIdentifier(List<String> lineIdentifier) {
        mLineIdentifier = lineIdentifier;
    }

    public String getNaptanIdReference() {
        return mNaptanIdReference;
    }

    public void setNaptanIdReference(String naptanIdReference) {
        mNaptanIdReference = naptanIdReference;
    }

    public String getStationAtcoCode() {
        return mStationAtcoCode;
    }

    public void setStationAtcoCode(String stationAtcoCode) {
        mStationAtcoCode = stationAtcoCode;
    }

}
