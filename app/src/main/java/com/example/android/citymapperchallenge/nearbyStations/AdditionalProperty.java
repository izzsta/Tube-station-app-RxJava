
package com.example.android.citymapperchallenge.nearbyStations;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AdditionalProperty {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("category")
    private String mCategory;
    @SerializedName("key")
    private String mKey;
    @SerializedName("sourceSystemKey")
    private String mSourceSystemKey;
    @SerializedName("value")
    private String mValue;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getSourceSystemKey() {
        return mSourceSystemKey;
    }

    public void setSourceSystemKey(String sourceSystemKey) {
        mSourceSystemKey = sourceSystemKey;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
