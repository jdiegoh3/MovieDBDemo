package com.example.moviesdbdemo.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moviesdbdemo.R;
import com.example.moviesdbdemo.models.Movie;
import com.example.moviesdbdemo.utils.StaticConstants;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.moviesdbdemo.utils.StringUtils.getStringResourceByName;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnMovieListeners mOnMovieListeners;
    private static final int MOVIE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    private List<Movie> mMovies;

    public MoviesRecyclerAdapter(OnMovieListeners onMovieListeners) {
        this.mOnMovieListeners = onMovieListeners;
        mMovies = new ArrayList<Movie>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType){
            case MOVIE_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_list_item, parent, false);
                return new MovieViewHolder(view, mOnMovieListeners);
            }
            case EXHAUSTED_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_item, parent, false);
                return new ExhaustedViewHolder(view);
            }
            case CATEGORY_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_type_item, parent, false);
                return new MovieTypeViewHolder(view, mOnMovieListeners);
            }
            case LOADING_TYPE:{
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_loading_item, parent, false);
                return new LoadingViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        Context context = viewHolder.itemView.getContext();
        if(itemViewType == MOVIE_TYPE){

            Glide.with(context).load(StaticConstants.IMAGE_BASE_URL + mMovies.get(i).getPosterPath())
                    .into(((MovieViewHolder) viewHolder).image);

            String votes = String.valueOf(mMovies.get(i).getVoteCount());
            ((MovieViewHolder)viewHolder).votes.setText(votes);
        } else if(itemViewType == CATEGORY_TYPE){

            Drawable imageDrawable = context.getDrawable(Integer.valueOf(mMovies.get(i).getPosterPath()));

            String categoryName = getStringResourceByName(context, mMovies.get(i).getTitle());
            ((MovieTypeViewHolder)viewHolder).categoryTitle.setText(categoryName);
            ((MovieTypeViewHolder)viewHolder).categoryImage.setImageDrawable(imageDrawable);
            ((MovieTypeViewHolder)viewHolder).setInternalTitle(mMovies.get(i).getTitle());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mMovies.get(position).getVoteCount() == -1){
            return CATEGORY_TYPE;
        } else if(mMovies.get(position).getTitle().equals("LOADINGID")){
            return LOADING_TYPE;
        } else if(mMovies.get(position).getTitle().equals("EXHAUSTEDID")){
            return EXHAUSTED_TYPE;
        }
        return MOVIE_TYPE;
    }

    @Override
    public int getItemCount() {
        return mMovies != null? mMovies.size() : 0;
    }

    public void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void displaySearchCategories(){
        List<Movie> categories = new ArrayList<>();

        for(int i = 0; i< StaticConstants.DEFAULT_TYPES.length; i++){
            Movie recipe = new Movie();
            recipe.setTitle(StaticConstants.DEFAULT_TYPES[i]);
            recipe.setPosterPath(String.valueOf(StaticConstants.DEFAULT_TYPES_IMAGES[i]));
            recipe.setVoteCount(-1);
            categories.add(recipe);
        }
        mMovies = categories;
        notifyDataSetChanged();
    }

    public void setQueryExhausted(){
        hideLoading();
        Movie exhaustedRecipe = new Movie();
        exhaustedRecipe.setTitle("EXHAUSTEDID");
        mMovies.add(exhaustedRecipe);
        notifyDataSetChanged();
    }

    public void displayLoading(){
        if(!isLoading()){
            Movie recipe = new Movie();
            recipe.setTitle("LOADINGID");
            List<Movie> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            mMovies = loadingList;
            notifyDataSetChanged();
        }
    }

    private void hideLoading(){
        if(isLoading()){
            for(Movie recipe: mMovies){
                if(recipe.getTitle().equals("LOADINGID")){
                    mMovies.remove(recipe);
                }
            }
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(mMovies != null){
            if(mMovies.size() > 0){
                if(mMovies.get(mMovies.size() - 1).getTitle().equals("LOADINGID")){
                    return true;
                }
            }
        }
        return false;
    }
}
