package com.example.moviesdbdemo.utils;

import com.example.moviesdbdemo.R;

public class StaticConstants {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    public static final String API_KEY = "b24b323885d7efaa27e0159c5764ab7d";

    public static final String[] DEFAULT_TYPES = {"upcoming", "top_rated", "popular"};

    public static final Integer[] DEFAULT_TYPES_IMAGES = {
            R.drawable.vector_upcoming,
            R.drawable.vector_top_rated,
            R.drawable.vector_popular
    };

}
