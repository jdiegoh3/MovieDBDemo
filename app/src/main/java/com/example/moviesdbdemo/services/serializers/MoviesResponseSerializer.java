package com.example.moviesdbdemo.services.serializers;

import com.example.moviesdbdemo.models.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponseSerializer {

    // <editor-fold desc="Serialize Fields">

    @SerializedName("results")
    @Expose
    private List<Movie> results;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    // </editor-fold>

    // <editor-fold desc="Getters/Setters">

    public List<Movie> getResults() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    // </editor-fold>
}
