package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Album {
    @SerializedName("album_id")
    @Expose
    String albumId;

    @SerializedName("album_name")
    @Expose
    String albumName;

    @SerializedName("tracks_num")
    @Expose
    int tracksNum;

    @SerializedName("followers_num")
    @Expose
    int followersNum;

    @SerializedName("listeners_num")
    @Expose
    int listenersNum;

    @SerializedName("duration")
    @Expose
    int duration;

    @SerializedName("rating")
    @Expose
    int rating;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("release_date")
    @Expose
    Date releaseDate;

    @SerializedName("is_private")
    @Expose
    boolean isPrivate;

    @SerializedName("last_updated")
    @Expose
    Date lastUpdated;

    @SerializedName("image_id")
    @Expose
    int imageId;

    @SerializedName("artist_id")
    @Expose
    int userId;

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getTracksNum() {
        return tracksNum;
    }

    public int getFollowersNum() {
        return followersNum;
    }

    public int getListenersNum() {
        return listenersNum;
    }

    public int getDuration() {
        return duration;
    }

    public int getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public int getImageId() {
        return imageId;
    }

    public int getUserId() {
        return userId;
    }
}