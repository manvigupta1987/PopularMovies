package com.example.manvi.movieappstage1.MoviesScreen;

import com.example.manvi.movieappstage1.BasePresenter;
import com.example.manvi.movieappstage1.BaseView;
import com.example.manvi.movieappstage1.data.Movie;

import java.util.ArrayList;

public interface MovieScreenContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showMovies(ArrayList<Movie> movieList);
        void showNoFavMovieError();
    }

    interface Presenter extends BasePresenter{
        void loadMovies(int page);
        void setFiltering(String tabType);
        String getFiltering();
    }
}
