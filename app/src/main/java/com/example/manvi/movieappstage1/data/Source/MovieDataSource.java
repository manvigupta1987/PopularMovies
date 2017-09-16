package com.example.manvi.movieappstage1.data.Source;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.data.Movie;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;

import java.util.List;

import rx.Observable;


/**
 * Created by manvi on 11/9/17.
 */

public interface MovieDataSource {

    Observable<List<Movie>> getMovies(String sortBy, int page);

    Observable<Movie> getMovie(String movieId);

    void insertMovie(@NonNull Movie movie);

    void deleteMovie(@NonNull Movie movie);

    Observable<List<Reviews>>getReviews(Long movieId);

    Observable<List<Trailer>>getTrailer(Long movieId);
}
