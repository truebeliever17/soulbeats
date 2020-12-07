package com.example.myapplication.network;

import com.example.myapplication.models.Album;
import com.example.myapplication.models.AlbumMainInfo;
import com.example.myapplication.models.AlbumRequest;
import com.example.myapplication.models.LoginRequest;
import com.example.myapplication.models.LoginResponse;
import com.example.myapplication.models.RegisterRequest;
import com.example.myapplication.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SoulbeatsApi {
    @GET("/albums")
    Call<List<Album>> getAllAlbums();

    @POST("/albums")
    Call<List<AlbumMainInfo>> getAlbumsWithMainInfo(@Body AlbumRequest albumRequest);

    @GET("/albums/my")
    Call<List<AlbumMainInfo>> getUserAlbumsByToken(@Header("token") String token);

    @GET("/users/me")
    Call<User> getUserByToken(@Header("token") String token);

    @POST("/users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/users/signup")
    Call <LoginResponse> register(@Body RegisterRequest registerRequest);
}
