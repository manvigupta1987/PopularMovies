package com.example.manvi.movieappstage1.data.Source;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.data.MovieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manvi on 11/9/17.
 */

public interface MovieDataSource {

    interface LoadMoviesCallback {

        void onMoviesLoaded(ArrayList<MovieData> movieList);

        void onDataNotAvailable();
    }

    interface GetMovieCallback {

        void onTaskLoaded(MovieData movieData );

        void onDataNotAvailable();
    }

    void getMovies(String sortBy, int page, @NonNull LoadMoviesCallback callback);

    void getMovie(@NonNull String movieId, @NonNull GetMovieCallback callback);

    void insertMovie(@NonNull MovieData movieData);

    void deleteAllMovies();

    void deleteMovie(@NonNull String movieId);

    void getReviewsTrailers(String movieId, @NonNull GetMovieCallback callback);
}
