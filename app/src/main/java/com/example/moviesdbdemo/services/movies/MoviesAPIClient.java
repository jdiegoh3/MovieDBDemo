package com.example.moviesdbdemo.services.movies;

import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;
import com.example.moviesdbdemo.utils.ApiService;
import com.example.moviesdbdemo.utils.StaticConstants;

import javax.inject.Singleton;
import io.reactivex.Observable;


@Singleton
public class MoviesAPIClient extends ApiService {

    private static final String TAG = MoviesAPIClient.class.getSimpleName();

    public void getUpComingMovies(Integer page, String language, ApiDisposable<MoviesResponseSerializer, Object> disposable){
        Observable<MoviesResponseSerializer> observable = getService(MoviesAPI.class)
                .getUpComingMovies(StaticConstants.API_KEY, page, language);
        handleRequest(observable, disposable);
    }


}
