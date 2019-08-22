package com.example.moviesdbdemo.ui.movies_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.moviesdbdemo.R;
import com.example.moviesdbdemo.ui.BaseActivity;

public class MovieListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);
    }
}
