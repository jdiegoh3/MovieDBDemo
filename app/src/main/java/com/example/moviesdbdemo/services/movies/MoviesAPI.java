package com.example.moviesdbdemo.services.movies;

import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;
import com.example.moviesdbdemo.services.serializers.MoviesVideoResponseSerializer;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("movie/{category}")
    Observable<MoviesResponseSerializer> getMovies(
            @Path("category") String category,
            @Query("api_key") String key,
            @Query("page") Integer page,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Observable<MoviesVideoResponseSerializer> getMovieVideo(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String key,
            @Query("language") String language
    );
}