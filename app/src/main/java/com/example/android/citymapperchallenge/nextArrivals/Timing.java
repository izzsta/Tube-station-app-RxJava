package com.example.android.citymapperchallenge.nextArrivals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timing {

    @SerializedName("$type")
    @Expose
    private String $type;
    @SerializedName("countdownServerAdjustment")
    @Expose
    private String countdownServerAdjustment;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("insert")
    @Expose
    private String insert;
    @SerializedName("read")
    @Expose
    private String read;
    @SerializedName("sent")
    @Expose
    private String sent;
    @SerializedName("received")
    @Expose
    private String received;

    public String get$type() {
        return $type;
    }

    public void set$type(String $type) {
        this.$type = $type;
    }

    public String getCountdownServerAdjustment() {
        return countdownServerAdjustment;
    }

    public void setCountdownServerAdjustment(String countdownServerAdjustment) {
        this.countdownServerAdjustment = countdownServerAdjustment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}

