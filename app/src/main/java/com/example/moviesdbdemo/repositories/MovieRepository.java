package com.example.moviesdbdemo.repositories;

import android.widget.Toast;

import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.services.movies.MoviesAPIClient;
import com.example.moviesdbdemo.services.serializers.MovieVideoSerializer;
import com.example.moviesdbdemo.services.serializers.MoviesResponseSerializer;
import com.example.moviesdbdemo.services.serializers.MoviesVideoResponseSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;

import java.util.List;

import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.example.moviesdbdemo.MoviesApplication.provideContext;

@Singleton
public class MovieRepository {

    // <editor-fold desc="Vars">

    private static MovieRepository movieRepository;
    private MoviesAPIClient mMoviesAPIClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Movie>> mMovies = new MediatorLiveData<>();

    // </editor-fold>

    // <editor-fold desc="Constructor">

    public MovieRepository() {
        mMoviesAPIClient = MoviesAPIClient.getInstance();
        initializeMediators();
    }

    // </editor-fold>

    // <editor-fold desc="Singleton">

    public static MovieRepository getInstance(){
        if(movieRepository == null){
            movieRepository = new MovieRepository();
        }
        return movieRepository;
    }

    // </editor-fold>

    // <editor-fold desc="LiveData Mediators">

    private void initializeMediators() {
        LiveData<List<Movie>> movieListApi = mMoviesAPIClient.getMovies();
        mMovies.addSource(movieListApi, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mMovies.setValue(movies);
            }
        });
    }

    public MediatorLiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    // </editor-fold>

    // <editor-fold desc="Search functions">

    public void searchMoviesApi(String query, int pageNumber){
        mPageNumber = pageNumber == 0? 1 : pageNumber;
        query = query == null? mQuery : query;
        mQuery = query;
        mIsQueryExhausted.setValue(false);
        mMoviesAPIClient.getMovies(pageNumber, query, new ApiDisposable<MoviesResponseSerializer, Object>(){
            @Override
            public void onNext(MoviesResponseSerializer moviesResponseSerializer) {
                super.onNext(moviesResponseSerializer);
                finishQuery(moviesResponseSerializer.getResults());
            }

            @Override
            public void onFailure(int statusCode, Object error) {
                super.onFailure(statusCode, error);
                switch (statusCode){
                    case 0:
                        Toast.makeText(provideContext(), "You need internet connection to get the movies :c", Toast.LENGTH_LONG).show();
                        break;
                    case 401:
                        Toast.makeText(provideContext(), "Authentication problem. Try again later.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    public void retrieveMovieVideo(Movie movie, final ApiDisposable<MovieVideoSerializer, Object> callback){
        mMoviesAPIClient.getMovieVideo(movie.getId(), new ApiDisposable<MoviesVideoResponseSerializer, Object>(){
            @Override
            public void onNext(MoviesVideoResponseSerializer moviesVideoResponseSerializer) {
                super.onNext(moviesVideoResponseSerializer);
                if(moviesVideoResponseSerializer.getVideos() != null
                        && moviesVideoResponseSerializer.getVideos().size() > 0) {
                    callback.onNext(moviesVideoResponseSerializer.getVideos().get(0));
                } else {
                    callback.onNext(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                callback.onNext(null);
            }

            @Override
            public void onFailure(int statusCode, Object error) {
                super.onFailure(statusCode, error);
                callback.onNext(null);
            }
        });
    }

    public void searchNextPage(){
        searchMoviesApi(mQuery, mPageNumber + 1);
    }

    // </editor-fold>

    // <editor-fold desc="Getters/Setters">

    public MutableLiveData<Boolean> getIsQueryExhausted() {
        return mIsQueryExhausted;
    }

    // </editor-fold>

    // <editor-fold desc="UI/UX">

    private void finishQuery(List<Movie> list){
        if(list != null){
            if (list.size() % 20 != 0) {
                mIsQueryExhausted.setValue(true);
            }
        }
        else{
            mIsQueryExhausted.setValue(true);
        }
    }

    // </editor-fold>

}
