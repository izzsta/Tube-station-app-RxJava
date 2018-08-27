package com.example.android.citymapperchallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextArrivals {

    @SerializedName("$type")
    @Expose
    private String $type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("operationType")
    @Expose
    private Long operationType;
    @SerializedName("vehicleId")
    @Expose
    private String vehicleId;
    @SerializedName("naptanId")
    @Expose
    private String naptanId;
    @SerializedName("stationName")
    @Expose
    private String stationName;
    @SerializedName("lineId")
    @Expose
    private String lineId;
    @SerializedName("lineName")
    @Expose
    private String lineName;
    @SerializedName("platformName")
    @Expose
    private String platformName;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("bearing")
    @Expose
    private String bearing;
    @SerializedName("destinationNaptanId")
    @Expose
    private String destinationNaptanId;
    @SerializedName("destinationName")
    @Expose
    private String destinationName;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("timeToStation")
    @Expose
    private Long timeToStation;
    @SerializedName("currentLocation")
    @Expose
    private String currentLocation;
    @SerializedName("towards")
    @Expose
    private String towards;
    @SerializedName("expectedArrival")
    @Expose
    private String expectedArrival;
    @SerializedName("timeToLive")
    @Expose
    private String timeToLive;
    @SerializedName("modeName")
    @Expose
    private String modeName;
    @SerializedName("timing")
    @Expose
    private Timing timing;

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getNaptanId() {
        return naptanId;
    }

    public void setNaptanId(String naptanId) {
        this.naptanId = naptanId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getDestinationNaptanId() {
        return destinationNaptanId;
    }

    public void setDestinationNaptanId(String destinationNaptanId) {
        this.destinationNaptanId = destinationNaptanId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimeToStation() {
        return timeToStation;
    }

    public void setTimeToStation(Long timeToStation) {
        this.timeToStation = timeToStation;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getTowards() {
        return towards;
    }

    public void setTowards(String towards) {
        this.towards = towards;
    }

    public String getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(String expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public String getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(String timeToLive) {
        this.timeToLive = timeToLive;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }
}
