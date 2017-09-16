package com.example.manvi.movieappstage1.data.Source;

import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;

import java.util.List;

import rx.Observable;


/**
 * Created by manvi on 11/9/17.
 */

public interface MovieDataSource {

    Observable<List<MovieData>> getMovies(String sortBy, int page);

    Observable<MovieData> getMovie(String movieId);

    void insertMovie(@NonNull MovieData movieData);

    void deleteMovie(@NonNull MovieData movieData);

    Observable<List<Reviews>>getReviews(Long movieId);

    Observable<List<Trailer>>getTrailer(Long movieId);
}
