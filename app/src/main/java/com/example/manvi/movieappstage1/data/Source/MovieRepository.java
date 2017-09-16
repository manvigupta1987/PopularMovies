package com.example.manvi.movieappstage1.data.Source;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.BuildConfig;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.MovieResponse;
import com.example.manvi.movieappstage1.data.ReviewResponse;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Source.remote.MovieService;
import com.example.manvi.movieappstage1.data.Trailer;
import com.example.manvi.movieappstage1.data.TrailerResponse;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 11/9/17.
 */

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
    public Observable<List<MovieData>> getMovies(String sortBy, int page) {
        checkNotNull(sortBy);

        if(sortBy.equals(ConstantsUtils.POPULAR_MOVIE) || sortBy.equals(ConstantsUtils.TOP_RATED_MOVIE))
        {
            Observable<MovieResponse> movieResponse = mMovieService.getMovies(sortBy, BuildConfig.API_KEY,page);
            return movieResponse.subscribeOn(Schedulers.io())
                    .map(new Func1<MovieResponse, List<MovieData>>() {
                        @Override
                        public List<MovieData> call(MovieResponse moviesResponse) {
                            return moviesResponse.getResults();
                        }
                    });
        }else {
            return mMoviesLocalDataSource.getMovies(sortBy,page);
        }
    }

    @Override
    public Observable<MovieData> getMovie(@NonNull String movieId) {
        checkNotNull(movieId);
        Observable<MovieData> movieData = mMoviesLocalDataSource.getMovie(movieId);
        return movieData;
    }

    @Override
    public void insertMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        mMoviesLocalDataSource.insertMovie(movieData);
    }

    @Override
    public void deleteMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        mMoviesLocalDataSource.deleteMovie(movieData);
    }

    @Override
    public Observable<List<Reviews>> getReviews(Long movieId) {
        checkNotNull(movieId);
        Observable<ReviewResponse> reviewResponse = mMovieService.getMovieReviews(movieId, BuildConfig.API_KEY);
        return reviewResponse.subscribeOn(Schedulers.io())
                .map(new Func1<ReviewResponse, List<Reviews>>() {
                    @Override
                    public List<Reviews> call(ReviewResponse reviewResponse1) {
                        return reviewResponse1.results;
                    }
                });
    }

    @Override
    public Observable<List<Trailer>>getTrailer(Long movieId) {
        checkNotNull(movieId);
        Observable<TrailerResponse> trailerResponse = mMovieService.getMovieTrailers(movieId, BuildConfig.API_KEY);
        return trailerResponse.subscribeOn(Schedulers.io())
                .map(new Func1<TrailerResponse, List<Trailer>>() {
                    @Override
                    public List<Trailer> call(TrailerResponse trailerResponse1) {
                        return trailerResponse1.results;
                    }
                });
    }
}
