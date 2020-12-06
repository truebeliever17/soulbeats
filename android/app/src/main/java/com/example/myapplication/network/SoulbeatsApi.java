package com.example.myapplication.network;

import com.example.myapplication.models.Album;
import com.example.myapplication.models.AlbumMainInfo;
import com.example.myapplication.models.AlbumRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SoulbeatsApi {
    @GET("/albums")
    Call<List<Album>> getAllAlbums();

    @POST("/albums")
    Call<List<AlbumMainInfo>> getAlbumsWithMainInfo(@Body AlbumRequest albumRequest);
}
