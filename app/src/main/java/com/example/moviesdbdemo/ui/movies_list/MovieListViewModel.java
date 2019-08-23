package com.example.moviesdbdemo.ui.movies_list;

import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.repositories.MovieRepository;
import com.example.moviesdbdemo.services.serializers.MovieVideoSerializer;
import com.example.moviesdbdemo.utils.ApiDisposable;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MovieListViewModel extends ViewModel {
    private MovieRepository mMovieRepository;
    private boolean mIsViewingMovies;
    private boolean mIsPerformingQuery;
    private boolean mIsPerformingSearch;

    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        mIsPerformingQuery = false;
        mIsPerformingSearch = false;
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

    public void setIsPerformingSearch(boolean mIsPerformingSearch) {
        this.mIsPerformingSearch = mIsPerformingSearch;
    }

    public boolean getIsPerformingSearch() {
        return mIsPerformingSearch;
    }

    public void setIsViewingMovies(boolean mIsViewingMovies) {
        this.mIsViewingMovies = mIsViewingMovies;
    }

    public List<Movie> performMoviesSearchLocal(String query){
        query = query.toLowerCase();
        List<Movie> actualMovieList = mMovieRepository.getMovies().getValue();
        List<Movie> result = new ArrayList<Movie>();
        if(actualMovieList != null) {
            for (Movie tempMovie : actualMovieList) {
                String title = tempMovie.getTitle();
                if(title != null && title.toLowerCase().contains(query)){
                    result.add(tempMovie);
                }
            }
        }
        return result;
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
            if(!getIsPerformingSearch()) {
                mMovieRepository.searchNextPage();
            }
        }
    }

    public void retrieveMovieVideo(Movie movie, ApiDisposable<MovieVideoSerializer, Object> callback){
        mMovieRepository.retrieveMovieVideo(movie, callback);
    }
}
