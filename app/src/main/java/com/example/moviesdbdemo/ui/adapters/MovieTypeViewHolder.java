package com.example.moviesdbdemo.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesdbdemo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView categoryImage;
    TextView categoryTitle;
    OnMovieListeners listener;

    public MovieTypeViewHolder(@NonNull View itemView, OnMovieListeners listener) {
        super(itemView);

        this.listener = listener;
        categoryTitle = itemView.findViewById(R.id.movie_type_title);
        categoryImage = itemView.findViewById(R.id.movie_type_image);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }

}
