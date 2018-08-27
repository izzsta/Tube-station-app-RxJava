
package com.example.android.citymapperchallenge.model.SequenceEndPoint;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LineSequence {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("direction")
    private String mDirection;
    @SerializedName("isOutboundOnly")
    private Boolean mIsOutboundOnly;
    @SerializedName("lineId")
    private String mLineId;
    @SerializedName("lineName")
    private String mLineName;
    @SerializedName("lineStrings")
    private List<String> mLineStrings;
    @SerializedName("mode")
    private String mMode;
    @SerializedName("orderedLineRoutes")
    private List<OrderedLineRoute> mOrderedLineRoutes;
    @SerializedName("stations")
    private List<Station> mStations;
    @SerializedName("stopPointSequences")
    private List<StopPointSequence> mStopPointSequences;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public String getDirection() {
        return mDirection;
    }

    public void setDirection(String direction) {
        mDirection = direction;
    }

    public Boolean getIsOutboundOnly() {
        return mIsOutboundOnly;
    }

    public void setIsOutboundOnly(Boolean isOutboundOnly) {
        mIsOutboundOnly = isOutboundOnly;
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

    public List<String> getLineStrings() {
        return mLineStrings;
    }

    public void setLineStrings(List<String> lineStrings) {
        mLineStrings = lineStrings;
    }

    public String getMode() {
        return mMode;
    }

    public void setMode(String mode) {
        mMode = mode;
    }

    public List<OrderedLineRoute> getOrderedLineRoutes() {
        return mOrderedLineRoutes;
    }

    public void setOrderedLineRoutes(List<OrderedLineRoute> orderedLineRoutes) {
        mOrderedLineRoutes = orderedLineRoutes;
    }

    public List<Station> getStations() {
        return mStations;
    }

    public void setStations(List<Station> stations) {
        mStations = stations;
    }

    public List<StopPointSequence> getStopPointSequences() {
        return mStopPointSequences;
    }

    public void setStopPointSequences(List<StopPointSequence> stopPointSequences) {
        mStopPointSequences = stopPointSequences;
    }

}
