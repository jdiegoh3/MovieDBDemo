package com.example.moviesdbdemo.services.movies;

import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("movie/upcoming")
    Observable<MoviesResponseSerializer> getUpComingMovies(
            @Query("api_key") String key,
            @Query("page") Integer page,
            @Query("language") String language
    );
}