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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesdbdemo.R;
import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.services.movies.MoviesAPIClient;
import com.example.moviesdbdemo.services.serializers.MovieVideoSerializer;
import com.example.moviesdbdemo.ui.BaseActivity;
import com.example.moviesdbdemo.ui.adapters.MovieViewHolder;
import com.example.moviesdbdemo.ui.adapters.MoviesRecyclerAdapter;
import com.example.moviesdbdemo.ui.adapters.OnMovieListeners;
import com.example.moviesdbdemo.utils.ApiDisposable;
import com.example.moviesdbdemo.utils.BottomSheetDialog;
import com.example.moviesdbdemo.utils.StaticConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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
        final Movie movie = mAdapter.getSelectedMovie(position);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "bottomMovie");
        bottomSheetDialog.setOnShowListener(new BottomSheetDialog.OnShowListener() {
            @Override
            public void OnShowListenerFn(View view) {
                ImageView movieImageView = view.findViewById(R.id.movieImageDetail);
                TextView movieTitle = view.findViewById(R.id.movieTitle);
                TextView movieOverView = view.findViewById(R.id.movieOverview);
                TextView movieDate = view.findViewById(R.id.movieReleasedDate);
                FloatingActionButton showVideo = view.findViewById(R.id.movieShowVideo);

                showVideo.hide();
                mMovieListViewModel.retrieveMovieVideo(movie, new ApiDisposable<MovieVideoSerializer, Object>(){
                    @Override
                    public void onNext(MovieVideoSerializer movieVideoSerializer) {
                        if(movieVideoSerializer != null){
                            showVideo.show();
                            showVideo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(movieVideoSerializer.getSite().equals("YouTube")) {
                                        String url = "https://www.youtube.com/watch?v=" + movieVideoSerializer.getKey();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                    } else if(movieVideoSerializer.getSite().equals("Vimeo")){
                                        String url = "https://vimeo.com/" + movieVideoSerializer.getKey();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                    }
                                }
                            });
                        }
                    }
                });

                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop();

                Glide.with(MovieListActivity.this).load(StaticConstants.IMAGE_BASE_URL + movie.getPosterPath())
                        .apply(requestOptions)
                        .centerCrop()
                        .into(movieImageView);

                movieTitle.setText(movie.getTitle());
                movieOverView.setText(movie.getOverview());
                movieDate.setText(movie.getReleaseDate());


            }
        });
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mMovieListViewModel.searchMoviesApi(category, 1);
        mSearchView.clearFocus();
    }
}
