
package com.example.android.citymapperchallenge.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class StationsWithinRadius {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("centrePoint")
    private List<Double> mCentrePoint;
    @SerializedName("page")
    private Long mPage;
    @SerializedName("pageSize")
    private Long mPageSize;
    @SerializedName("stopPoints")
    private List<StopPoint> mStopPoints;
    @SerializedName("total")
    private Long mTotal;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public List<Double> getCentrePoint() {
        return mCentrePoint;
    }

    public void setCentrePoint(List<Double> centrePoint) {
        mCentrePoint = centrePoint;
    }

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public Long getPageSize() {
        return mPageSize;
    }

    public void setPageSize(Long pageSize) {
        mPageSize = pageSize;
    }

    public List<StopPoint> getStopPoints() {
        return mStopPoints;
    }

    public void setStopPoints(List<StopPoint> stopPoints) {
        mStopPoints = stopPoints;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
