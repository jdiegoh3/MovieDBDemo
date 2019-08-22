package com.example.moviesdbdemo.ui.adapters;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesdbdemo.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView votes;
    AppCompatImageView image;
    ProgressBar loadingImage;
    OnMovieListeners onMovieListeners;

    public MovieViewHolder(@NonNull View itemView, OnMovieListeners onMovieListeners) {
        super(itemView);
        this.onMovieListeners = onMovieListeners;

        votes = itemView.findViewById(R.id.votes_text);
        image = itemView.findViewById(R.id.movie_image);
        loadingImage = itemView.findViewById(R.id.progressImageLoading);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListeners.onMovieClick(getAdapterPosition());
    }
}
