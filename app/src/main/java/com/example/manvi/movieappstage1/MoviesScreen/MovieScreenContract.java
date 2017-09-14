package com.example.manvi.movieappstage1.MoviesScreen;

import com.example.manvi.movieappstage1.BasePresenter;
import com.example.manvi.movieappstage1.BaseView;
import com.example.manvi.movieappstage1.data.MovieData;

import java.util.ArrayList;

/**
 * Created by manvi on 11/9/17.
 */

public interface MovieScreenContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showMovies(ArrayList<MovieData> movieList);
        void showMovieDetailsUI(MovieData movieData);
        void showNoFavMovieError();
    }

    interface Presenter extends BasePresenter{
        void loadMovies(int page);
        void openMovieDetails(MovieData movieData);
        void setFiltering(String tabType);
        String getFiltering();

    }
}