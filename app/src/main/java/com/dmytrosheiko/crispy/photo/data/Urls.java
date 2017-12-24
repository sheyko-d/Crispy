package com.dmytrosheiko.crispy.photo.data;

import com.google.gson.annotations.SerializedName;


/**
 * Immutable model class for a Photo.
 */
public final class Urls {

    @SerializedName("raw")
    private final String mRaw;
    @SerializedName("full")
    private final String mFull;
    @SerializedName("regular")
    private final String mRegular;
    @SerializedName("small")
    private final String mSmall;

    public Urls(String raw, String full, String regular, String small) {
        mRaw = raw;
        mFull = full;
        mRegular = regular;
        mSmall = small;
    }

    public String getRaw() {
        return mRaw;
    }

    public String getFull() {
        return mFull;
    }

    public String getRegular() {
        return mRegular;
    }

    public String getSmall() {
        return mSmall;
    }
}
