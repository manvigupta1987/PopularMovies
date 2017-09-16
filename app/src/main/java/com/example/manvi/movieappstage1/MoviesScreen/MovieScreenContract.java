package com.example.manvi.movieappstage1.MoviesScreen;

import com.example.manvi.movieappstage1.BasePresenter;
import com.example.manvi.movieappstage1.BaseView;
import com.example.manvi.movieappstage1.data.Movie;

import java.util.ArrayList;

/**
 * Created by manvi on 11/9/17.
 */

public interface MovieScreenContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showMovies(ArrayList<Movie> movieList);
        void showMovieDetailsUI(Movie movie, View view);
        void showNoFavMovieError();
    }

    interface Presenter extends BasePresenter{
        void loadMovies(int page);
        void openMovieDetails(Movie movie, View view);
        void setFiltering(String tabType);
        String getFiltering();
    }
}
