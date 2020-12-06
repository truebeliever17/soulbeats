package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumRequest {
    @SerializedName("show_only_main_info")
    @Expose
    boolean showMainInfo;

    public AlbumRequest(boolean showMainInfo) {
        this.showMainInfo = showMainInfo;
    }
}
