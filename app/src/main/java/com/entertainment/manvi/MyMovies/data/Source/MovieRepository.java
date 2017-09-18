package com.entertainment.manvi.MyMovies.data.Source;

import android.support.annotation.NonNull;

import com.entertainment.manvi.MyMovies.BuildConfig;
import com.entertainment.manvi.MyMovies.Utils.ConstantsUtils;
import com.entertainment.manvi.MyMovies.data.Movie;
import com.entertainment.manvi.MyMovies.data.MovieResponse;
import com.entertainment.manvi.MyMovies.data.ReviewResponse;
import com.entertainment.manvi.MyMovies.data.Reviews;
import com.entertainment.manvi.MyMovies.data.Source.remote.MovieService;
import com.entertainment.manvi.MyMovies.data.Trailer;
import com.entertainment.manvi.MyMovies.data.TrailerResponse;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class MovieRepository implements MovieDataSource {

    private static MovieRepository INSTANCE =null;


    private final MovieDataSource mMoviesLocalDataSource;

    private final MovieService mMovieService;

    // Prevent direct instantiation.
    private MovieRepository(@NonNull MovieDataSource moviesLocalDataSource,
                            @NonNull MovieService movieService) {
        mMoviesLocalDataSource = checkNotNull(moviesLocalDataSource);
        mMovieService = checkNotNull(movieService);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *

     * @return the {@link MovieRepository} instance
     */
    public static MovieRepository getInstance(MovieDataSource moviesLocalDataSource,
                                              MovieService movieService) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRepository(moviesLocalDataSource, movieService);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #(MovieDataSource, MovieDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public Observable<List<Movie>> getMovies(String sortBy, int page) {
        checkNotNull(sortBy);

        if(sortBy.equals(ConstantsUtils.POPULAR_MOVIE) || sortBy.equals(ConstantsUtils.TOP_RATED_MOVIE))
        {
            Observable<MovieResponse> movieResponse = mMovieService.getMovies(sortBy, BuildConfig.API_KEY,page);
            return movieResponse.subscribeOn(Schedulers.io())
                    .map(MovieResponse::getResults);
        }else {
            return mMoviesLocalDataSource.getMovies(sortBy,page);
        }
    }

    @Override
    public Observable<Movie> getMovie(@NonNull String movieId) {
        checkNotNull(movieId);
        return mMoviesLocalDataSource.getMovie(movieId);
    }

    @Override
    public void insertMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesLocalDataSource.insertMovie(movie);
    }

    @Override
    public void deleteMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        mMoviesLocalDataSource.deleteMovie(movie);
    }

    @Override
    public Observable<List<Reviews>> getReviews(Long movieId) {
        checkNotNull(movieId);
        Observable<ReviewResponse> reviewResponse = mMovieService.getMovieReviews(movieId, BuildConfig.API_KEY);
        return reviewResponse.subscribeOn(Schedulers.io())
                .map(reviewResponse1 -> reviewResponse1.results);
    }

    @Override
    public Observable<List<Trailer>>getTrailer(Long movieId) {
        checkNotNull(movieId);
        Observable<TrailerResponse> trailerResponse = mMovieService.getMovieTrailers(movieId, BuildConfig.API_KEY);
        return trailerResponse.subscribeOn(Schedulers.io())
                .map(trailerResponse1 -> trailerResponse1.results);
    }
}
