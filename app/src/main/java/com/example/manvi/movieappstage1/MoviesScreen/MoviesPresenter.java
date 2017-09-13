package com.example.manvi.movieappstage1.MoviesScreen;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 12/9/17.
 */

public class MoviesPresenter implements MovieScreenContract.Presenter {

    private final MovieRepository mMoviesRepository;
    private final MovieScreenContract.View mMoviesView;
    private String mCurrentFiltering = ConstantsUtils.POPULAR_MOVIE;


    public MoviesPresenter(@NonNull MovieRepository moviesRepository, @NonNull MovieScreenContract.View moviesView) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        mMoviesView.setPresenter(this);
    }

    @Override
    public void start(int page) {
        loadMovies(page);
    }

    @Override
    public void loadMovies(int page) {
        mMoviesView.setLoadingIndicator(true);
        mMoviesRepository.getMovies(mCurrentFiltering, page, new MovieDataSource.LoadMoviesCallback() {

            @Override
            public void onMoviesLoaded(ArrayList<MovieData> movieList) {
                if(movieList!=null && !movieList.isEmpty()){
                    mMoviesView.setLoadingIndicator(false);
                    mMoviesView.showMovies(movieList);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if(mCurrentFiltering == ConstantsUtils.FAVORITE_MOVIE) {
                    mMoviesView.showNoFavMovieError();
                }
            }
        });
    }

    @Override
    public void openMovieDetails(MovieData movieData) {
        checkNotNull(movieData, "requestedMovie cannot be null!");
        mMoviesView.showMovieDetailsUI(movieData);
    }

    @Override
    public void setFiltering(String movieFilter) {
        mCurrentFiltering = movieFilter;
    }


    @Override
    public String getFiltering() {
        return mCurrentFiltering;
    }
}
