package com.example.manvi.movieappstage1.data.Source;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieData;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 11/9/17.
 */

public class MovieRepository implements MovieDataSource {

    private static MovieRepository INSTANCE =null;

    private final MovieDataSource mMoviesRemoteDataSource;

    private final MovieDataSource mMoviesLocalDataSource;

    // Prevent direct instantiation.
    private MovieRepository(@NonNull MovieDataSource moviesRemoteDataSource,
                            @NonNull MovieDataSource moviesLocalDataSource) {
        mMoviesRemoteDataSource = checkNotNull(moviesRemoteDataSource);
        mMoviesLocalDataSource = checkNotNull(moviesLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param moviesRemoteDataSource the backend data source
     * @param moviesRemoteDataSource  the device storage data source
     * @return the {@link MovieRepository} instance
     */
    public static MovieRepository getInstance(MovieDataSource moviesRemoteDataSource,
                                              MovieDataSource moviesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(moviesRemoteDataSource, moviesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(MovieDataSource, MovieDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public void getMovies(String sortBy, int page, @NonNull final LoadMoviesCallback callback) {
        checkNotNull(callback);
        checkNotNull(sortBy);

        if(sortBy.equals(ConstantsUtils.POPULAR_MOVIE) || sortBy.equals(ConstantsUtils.TOP_RATED_MOVIE))
        {
            mMoviesRemoteDataSource.getMovies(sortBy,page,new LoadMoviesCallback() {

                @Override
                public void onMoviesLoaded(ArrayList<MovieData> movieList) {
                    callback.onMoviesLoaded(movieList);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }else {
            mMoviesLocalDataSource.getMovies(sortBy, page, new LoadMoviesCallback() {
                @Override
                public void onMoviesLoaded(ArrayList<MovieData> movieList) {
                    callback.onMoviesLoaded(movieList);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getMovie(@NonNull String movieId, @NonNull final GetMovieCallback callback) {
        checkNotNull(movieId);
        checkNotNull(callback);
        mMoviesLocalDataSource.getMovie(movieId, new GetMovieCallback() {
            @Override
            public void onTaskLoaded(MovieData movieData) {
                callback.onTaskLoaded(movieData);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void insertMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        mMoviesLocalDataSource.insertMovie(movieData);
    }

    @Override
    public void deleteAllMovies() {

    }

    @Override
    public void deleteMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        mMoviesLocalDataSource.deleteMovie(movieData);
    }

    @Override
    public void getReviewsTrailers(Long movieId, @NonNull final GetMovieCallback callback) {
        checkNotNull(movieId);
        checkNotNull(callback);
        mMoviesRemoteDataSource.getReviewsTrailers(movieId,new GetMovieCallback() {

            @Override
            public void onTaskLoaded(MovieData movieData) {
                callback.onTaskLoaded(movieData);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
