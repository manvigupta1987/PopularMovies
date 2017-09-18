package com.entertainment.manvi.MyMovies.MoviesScreen;

import com.entertainment.manvi.MyMovies.BasePresenter;
import com.entertainment.manvi.MyMovies.BaseView;
import com.entertainment.manvi.MyMovies.data.Movie;

import java.util.ArrayList;

public interface MovieScreenContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showMovies(ArrayList<Movie> movieList);
        void showNoMovieError();
        boolean isOnline();
        void showProgress();
    }

    interface Presenter extends BasePresenter{
        void loadMovies(int page);
        void setFiltering(String tabType);
        String getFiltering();
    }
}
