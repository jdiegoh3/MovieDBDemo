package com.example.moviesdbdemo.ui.movies_list;

import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.repositories.MovieRepository;
import com.example.moviesdbdemo.services.serializers.MovieVideoSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MovieListViewModel extends ViewModel {
    private MovieRepository mMovieRepository;
    private boolean mIsViewingMovies;
    private boolean mIsPerformingQuery;

    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Movie>> getMovies(){
        return mMovieRepository.getMovies();
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mMovieRepository.getIsQueryExhausted();
    }

    public void getMoviesApi(Integer page){
        mIsViewingMovies = true;
        mIsPerformingQuery = true;

    }

    public boolean getIsViewingMovies() {
        return mIsViewingMovies;
    }

    public void setIsPerformingQuery(boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public void setIsViewingMovies(boolean mIsViewingMovies) {
        this.mIsViewingMovies = mIsViewingMovies;
    }

    public void searchMoviesApi(String query, int page){
        mIsViewingMovies = true;
        mIsPerformingQuery = true;
        mMovieRepository.searchMoviesApi(query, page);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery
                && mIsViewingMovies
                && !isQueryExhausted().getValue()){
            mMovieRepository.searchNextPage();
        }
    }

    public void retrieveMovieVideo(Movie movie, ApiDisposable<MovieVideoSerializer, Object> callback){
        mMovieRepository.retrieveMovieVideo(movie, callback);
    }
}
