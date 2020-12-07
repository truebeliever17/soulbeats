package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    @Expose
    String username;

    @SerializedName("display_name")
    @Expose
    String fullName;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("password")
    @Expose
    String password;

    public RegisterRequest(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
