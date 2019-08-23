package com.example.moviesdbdemo.services.serializers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesVideoResponseSerializer {
    @SerializedName("results")
    @Expose
    List<MovieVideoSerializer> videos;

    public List<MovieVideoSerializer> getVideos() {
        return videos;
    }
}
