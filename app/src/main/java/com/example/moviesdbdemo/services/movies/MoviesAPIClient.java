package com.example.moviesdbdemo.services.movies;

import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;
import com.example.moviesdbdemo.utils.ApiDisposableWrapper;
import com.example.moviesdbdemo.utils.ApiService;
import com.example.moviesdbdemo.utils.StaticConstants;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;


public class MoviesAPIClient extends ApiService {

    private static final String TAG = MoviesAPIClient.class.getSimpleName();

    private static MoviesAPIClient moviesAPIClient;
    private MutableLiveData<Movie> mMovie = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    public static MoviesAPIClient getInstance() {
        if(moviesAPIClient == null){
            moviesAPIClient = new MoviesAPIClient();
        }
        return moviesAPIClient;
    }

    public void getMovies(final Integer page, String language, String category, ApiDisposable<MoviesResponseSerializer, Object> disposable){
        Observable<MoviesResponseSerializer> observable = getService(MoviesAPI.class)
                .getMovies(category, StaticConstants.API_KEY, page, language);
        handleRequest(observable, new ApiDisposableWrapper<MoviesResponseSerializer, Object>(disposable){
            @Override
            public void onNext(MoviesResponseSerializer response) {
                if(page == 1){
                    mMovies.postValue(response.getResults());
                } else {
                    List<Movie> currentMovies = mMovies.getValue();
                    currentMovies.addAll(response.getResults());
                    mMovies.postValue(currentMovies);
                }
                super.onNext(response);
            }
        });
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return mMovies;
    }
}
