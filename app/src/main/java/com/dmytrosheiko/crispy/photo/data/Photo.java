package com.dmytrosheiko.crispy.photo.data;

import com.google.gson.annotations.SerializedName;


/**
 * Immutable model class for a Photo.
 */
public final class Photo {

    @SerializedName("id")
    private final String mId;
    @SerializedName("urls")
    private final Urls mUrls;
    @SerializedName("color")
    private final String mColor;

    public Photo(String id, Urls urls, String color) {
        mId = id;
        mUrls = urls;
        mColor = color;
    }

    public String getId() {
        return mId;
    }

    public String getRawUrl() {
        return mUrls.getRaw();
    }

    public String getFullUrl() {
        return mUrls.getFull();
    }

    public String getRegularUrl() {
        return mUrls.getRegular();
    }

    public String getSmallUrl() {
        return mUrls.getSmall();
    }

    public String getColor(){
        return mColor;
    }
}
