package com.example.myapplication.network;

import com.example.myapplication.models.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SoulbeatsApi {
    @GET("/albums")
    Call<List<Album>> getAllAlbums();
}
