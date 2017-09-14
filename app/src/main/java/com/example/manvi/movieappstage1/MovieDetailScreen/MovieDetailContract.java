package com.example.manvi.movieappstage1.MovieDetailScreen;

import com.example.manvi.movieappstage1.BasePresenter;
import com.example.manvi.movieappstage1.BaseView;
import com.example.manvi.movieappstage1.MoviesScreen.MovieScreenContract;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;

import java.util.ArrayList;

/**
 * Created by manvi on 13/9/17.
 */

public interface MovieDetailContract {

    interface View extends BaseView<MovieDetailContract.Presenter> {
        void showMovieDetails(String overView, String title,
                              String releaseDate, Double voteAvg,
                              Integer voteCount, String lang,
                              String poster_path, String backDropImagePath);

        void showsTrailers(boolean isTrailerPresent);
        void showReviews(boolean isReviewPresent);
        void showMovieStatus(boolean isFavourite);
        void setupTrailerRecyclerView();
        void setupReviewLayout();
        void showDefaultTextView();
        void shareMovie(String title, String urlLink);
        void showTrailersData(ArrayList<Trailer> trailers);
        void showReviewsData(ArrayList<Reviews> reviews);

    }

    interface Presenter extends BasePresenter{
        void loadReviewsNTrailer();
        void addMovieToFavourite();
        void removeMovieFromFavourite();
        void checkForFavouriteMovie();
        void shareMovie();
        void showMovieDetails();
    }
}
