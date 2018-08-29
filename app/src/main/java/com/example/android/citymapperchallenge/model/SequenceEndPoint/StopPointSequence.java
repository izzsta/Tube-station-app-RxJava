
package com.example.android.citymapperchallenge.model.SequenceEndPoint;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class StopPointSequence {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("branchId")
    private Long mBranchId;
    @SerializedName("direction")
    private String mDirection;
    @SerializedName("lineId")
    private String mLineId;
    @SerializedName("lineName")
    private String mLineName;
    @SerializedName("nextBranchIds")
    private List<Object> mNextBranchIds;
    @SerializedName("prevBranchIds")
    private List<Object> mPrevBranchIds;
    @SerializedName("serviceType")
    private String mServiceType;
    @SerializedName("stopPoint")
    private List<StopPoint> mStopPoint;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public Long getBranchId() {
        return mBranchId;
    }

    public void setBranchId(Long branchId) {
        mBranchId = branchId;
    }

    public String getDirection() {
        return mDirection;
    }

    public void setDirection(String direction) {
        mDirection = direction;
    }

    public String getLineId() {
        return mLineId;
    }

    public void setLineId(String lineId) {
        mLineId = lineId;
    }

    public String getLineName() {
        return mLineName;
    }

    public void setLineName(String lineName) {
        mLineName = lineName;
    }

    public List<Object> getNextBranchIds() {
        return mNextBranchIds;
    }

    public void setNextBranchIds(List<Object> nextBranchIds) {
        mNextBranchIds = nextBranchIds;
    }

    public List<Object> getPrevBranchIds() {
        return mPrevBranchIds;
    }

    public void setPrevBranchIds(List<Object> prevBranchIds) {
        mPrevBranchIds = prevBranchIds;
    }

    public String getServiceType() {
        return mServiceType;
    }

    public void setServiceType(String serviceType) {
        mServiceType = serviceType;
    }

    public List<StopPoint> getStopPoint() {
        return mStopPoint;
    }

    public void setStopPoint(List<StopPoint> stopPoint) {
        mStopPoint = stopPoint;
    }

}
