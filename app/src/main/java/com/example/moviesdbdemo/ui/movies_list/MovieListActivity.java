package com.example.moviesdbdemo.ui.movies_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.Toast;

import com.example.moviesdbdemo.R;
import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.services.movies.MoviesAPIClient;
import com.example.moviesdbdemo.ui.BaseActivity;
import com.example.moviesdbdemo.ui.adapters.MoviesRecyclerAdapter;
import com.example.moviesdbdemo.ui.adapters.OnMovieListeners;

import java.util.List;


public class MovieListActivity extends BaseActivity implements OnMovieListeners {

    @BindView(R.id.search_view)
    SearchView mSearchView;

    @BindView(R.id.movies_list)
    RecyclerView mRecyclerView;

    private static final String TAG = MovieListActivity.class.getSimpleName();
    private MovieListViewModel mMovieListViewModel;
    private MoviesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);
        ButterKnife.bind(this);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);
        initRecycler();
        subscribeObservers();
        initSearchView();
        if(!mMovieListViewModel.getIsViewingMovies()){
            displaySearchCategories();
        }
    }

    private void displaySearchCategories() {
        mMovieListViewModel.setIsViewingMovies(false);
        mAdapter.displaySearchCategories();
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.displayLoading();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers() {
        mMovieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(movies != null){
                    if(mMovieListViewModel.getIsViewingMovies()){
                        mMovieListViewModel.setIsPerformingQuery(false);
                        mAdapter.setMovies(movies);
                    }
                }
            }
        });

        mMovieListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void initRecycler() {
        mAdapter = new MoviesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!mRecyclerView.canScrollVertically(1)){
                    mMovieListViewModel.searchNextPage();
                }
            }
        });

        mAdapter.setQueryExhausted();

    }

    @Override
    public void onBackPressed() {
        if(mMovieListViewModel.getIsViewingMovies()){
            displaySearchCategories();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMovieClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mMovieListViewModel.searchMoviesApi(category, 1);
        mSearchView.clearFocus();
    }
}
