package com.dmytrosheiko.crispy.network;

import com.dmytrosheiko.crispy.photo.data.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/search/photos?page=1&per_page=30&query=christmas")
    Call<SearchResponse> getNewPhotos();
}
