
package com.example.android.citymapperchallenge.model.NearbyEndPoint;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LineModeGroup {

    @SerializedName("$type")
    private String m$type;
    @SerializedName("lineIdentifier")
    private List<String> mLineIdentifier;
    @SerializedName("modeName")
    private String mModeName;

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

    public String getModeName() {
        return mModeName;
    }

    public void setModeName(String modeName) {
        mModeName = modeName;
    }

}
