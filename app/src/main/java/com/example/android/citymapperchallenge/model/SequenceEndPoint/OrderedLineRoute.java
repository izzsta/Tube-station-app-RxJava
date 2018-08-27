
package com.example.android.citymapperchallenge.model.SequenceEndPoint;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OrderedLineRoute {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("name")
    private String mName;
    @SerializedName("naptanIds")
    private List<String> mNaptanIds;
    @SerializedName("serviceType")
    private String mServiceType;

    public String get$type() {
        return m$type;
    }

    public void set$type(String $type) {
        m$type = $type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<String> getNaptanIds() {
        return mNaptanIds;
    }

    public void setNaptanIds(List<String> naptanIds) {
        mNaptanIds = naptanIds;
    }

    public String getServiceType() {
        return mServiceType;
    }

    public void setServiceType(String serviceType) {
        mServiceType = serviceType;
    }

}
