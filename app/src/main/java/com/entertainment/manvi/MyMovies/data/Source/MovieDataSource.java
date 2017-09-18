package com.entertainment.manvi.MyMovies.data.Source;

import android.support.annotation.NonNull;

import com.entertainment.manvi.MyMovies.data.Movie;
import com.entertainment.manvi.MyMovies.data.Reviews;
import com.entertainment.manvi.MyMovies.data.Trailer;

import java.util.List;

import rx.Observable;

public interface MovieDataSource {

    Observable<List<Movie>> getMovies(String sortBy, int page);

    Observable<Movie> getMovie(String movieId);

    void insertMovie(@NonNull Movie movie);

    void deleteMovie(@NonNull Movie movie);

    Observable<List<Reviews>>getReviews(Long movieId);

    Observable<List<Trailer>>getTrailer(Long movieId);
}
