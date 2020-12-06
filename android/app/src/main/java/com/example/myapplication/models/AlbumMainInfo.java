package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumMainInfo {
    @SerializedName("image_id")
    @Expose
    int imageId;

    @SerializedName("album_name")
    @Expose
    String albumName;

    @SerializedName("display_name")
    @Expose
    String artistName;

    public int getImageId() {
        return imageId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }
}
