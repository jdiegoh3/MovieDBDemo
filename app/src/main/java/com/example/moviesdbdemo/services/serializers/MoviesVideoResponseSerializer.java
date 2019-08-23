package com.example.moviesdbdemo.services.serializers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesVideoResponseSerializer {

    // <editor-fold desc="Serialize Fields">

    @SerializedName("results")
    @Expose
    List<MovieVideoSerializer> videos;

    // </editor-fold>

    // <editor-fold desc="Getters/Setters">

    public List<MovieVideoSerializer> getVideos() {
        return videos;
    }

    // </editor-fold>
}
