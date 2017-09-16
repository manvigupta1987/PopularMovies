package com.example.manvi.movieappstage1.data.Source.remote;

import com.example.manvi.movieappstage1.data.MovieResponse;
import com.example.manvi.movieappstage1.data.ReviewResponse;
import com.example.manvi.movieappstage1.data.TrailerResponse;


import retrofit2.http.Path;
import rx.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by manvi on 14/9/17.
 */

public interface MovieService {
    @GET("movie/{sortBy}")
    Observable<MovieResponse> getMovies(
            @Path("sortBy") String sortBy,
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getMovieReviews(@Path("id") long movieId, @Query("api_key") String key);

    @GET("movie/{id}/videos")
    Observable<TrailerResponse> getMovieTrailers(@Path("id") long movieId, @Query("api_key") String key);
}
