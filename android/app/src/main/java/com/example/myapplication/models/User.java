package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class User {
    @SerializedName("user_id")
    @Expose
    int userId;

    @SerializedName("username")
    @Expose
    String userName;

    @SerializedName("display_name")
    @Expose
    String displayName;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("password")
    @Expose
    String password;

    @SerializedName("reg_date")
    @Expose
    Date regDate;

    @SerializedName("is_verified")
    @Expose
    boolean isVerified;

    @SerializedName("country_id")
    @Expose
    int countryId;

    @SerializedName("last_track_id")
    @Expose
    int lastTrackId;

    @SerializedName("last_track_stopped_time")
    @Expose
    int lastTrackStopTime;

    @SerializedName("subscription_id")
    @Expose
    int subscriptionId;

    @SerializedName("image_id")
    @Expose
    int imageId;

    @SerializedName("last_login_date")
    @Expose
    Date lastLoginDate;

    @SerializedName("last_activity_date")
    @Expose
    Date lastActivityDate;

    @SerializedName("last_updated")
    @Expose
    Date lastUpdated;

    @SerializedName("birth_date")
    @Expose
    Date birthDate;

    @SerializedName("description")
    @Expose
    String description;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getRegDate() {
        return regDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public int getCountryId() {
        return countryId;
    }

    public int getLastTrackId() {
        return lastTrackId;
    }

    public int getLastTrackStopTime() {
        return lastTrackStopTime;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public int getImageId() {
        return imageId;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getDescription() {
        return description;
    }
}
