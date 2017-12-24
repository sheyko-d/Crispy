package com.dmytrosheiko.crispy.photo.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Immutable model class for a SearchResponse.
 */
public final class SearchResponse {

    @SerializedName("results")
    private final ArrayList<Photo> mResults;

    public SearchResponse(ArrayList<Photo> results) {
        mResults = results;
    }

    public ArrayList<Photo> getResults() {
        return mResults;
    }
}
