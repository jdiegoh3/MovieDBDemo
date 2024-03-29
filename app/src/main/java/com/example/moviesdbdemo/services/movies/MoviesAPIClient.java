package com.example.moviesdbdemo.services.movies;

import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;
import com.example.moviesdbdemo.services.serializers.MoviesVideoResponseSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;
import com.example.moviesdbdemo.utils.ApiDisposableWrapper;
import com.example.moviesdbdemo.utils.ApiService;
import com.example.moviesdbdemo.utils.StaticConstants;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;

import static com.example.moviesdbdemo.utils.StringUtils.getCurrentLanguage;


public class MoviesAPIClient extends ApiService {

    // <editor-fold desc="Vars">

    private static final String TAG = MoviesAPIClient.class.getSimpleName();
    private static MoviesAPIClient moviesAPIClient;
    private MutableLiveData<Movie> mMovie = new MutableLiveData<>();
    private MutableLiveData<List<Movie>> mMovies = new MutableLiveData<>();

    // </editor-fold>

    // <editor-fold desc="Singleton">

    public static MoviesAPIClient getInstance() {
        if(moviesAPIClient == null){
            moviesAPIClient = new MoviesAPIClient();
        }
        return moviesAPIClient;
    }

    // </editor-fold>

    // <editor-fold desc="API Calls">

    public void getMovies(final Integer page, String category, ApiDisposable<MoviesResponseSerializer, Object> disposable){
        Observable<MoviesResponseSerializer> observable = getService(MoviesAPI.class)
                .getMovies(category, StaticConstants.API_KEY, page, getCurrentLanguage());
        handleRequest(observable, new ApiDisposableWrapper<MoviesResponseSerializer, Object>(disposable){
            @Override
            public void onNext(MoviesResponseSerializer response) {
                // Emit postValue to LiveData
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

    public void getMovieVideo(Integer movieId, ApiDisposable<MoviesVideoResponseSerializer, Object> disposable){
        Observable<MoviesVideoResponseSerializer> observable = getService(MoviesAPI.class)
                .getMovieVideo(movieId, StaticConstants.API_KEY, getCurrentLanguage());
        handleRequest(observable, disposable);
    }

    public void searchMovie(final Integer page, String query, ApiDisposable<MoviesResponseSerializer, Object> disposable){
        Observable<MoviesResponseSerializer> observable = getService(MoviesAPI.class)
                .searchMovie(StaticConstants.API_KEY, page, getCurrentLanguage(), query);
    }

    // </editor-fold>

    // <editor-fold desc="Getter/Setters">

    public MutableLiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    // </editor-fold>
}
