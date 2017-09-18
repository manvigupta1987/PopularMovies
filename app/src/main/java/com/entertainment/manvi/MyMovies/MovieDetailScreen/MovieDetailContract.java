package com.entertainment.manvi.MyMovies.MovieDetailScreen;

import com.entertainment.manvi.MyMovies.BasePresenter;
import com.entertainment.manvi.MyMovies.BaseView;
import com.entertainment.manvi.MyMovies.data.Reviews;
import com.entertainment.manvi.MyMovies.data.Trailer;

import java.util.ArrayList;

public interface MovieDetailContract {

    interface View extends BaseView<MovieDetailContract.Presenter> {
        void showMovieDetails(String overView, String title,
                              String releaseDate, Double voteAvg,
                              Integer voteCount, String lang,
                              String poster_path, String backDropImagePath);

        void showsTrailers(boolean isTrailerPresent);
        void showReviews(boolean isReviewPresent);
        void showMovieStatus(boolean isFavourite);
        //void setupTrailerRecyclerView();
        void setupReviewLayout();
        void shareMovie(String title, String urlLink);
        void showTrailersData(ArrayList<Trailer> trailers);
        void showReviewsData(ArrayList<Reviews> reviews);
    }

    interface Presenter extends BasePresenter{
        void loadReviews();
        void loadTrailers();
        void addMovieToFavourite();
        void removeMovieFromFavourite();
        void checkForFavouriteMovie();
        void shareMovie();
        void showMovieDetails();
    }
}
