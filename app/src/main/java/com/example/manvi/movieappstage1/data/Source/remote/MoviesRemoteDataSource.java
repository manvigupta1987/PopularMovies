package com.example.manvi.movieappstage1.data.Source.remote;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.BuildConfig;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by manvi on 11/9/17.
 */

public class MoviesRemoteDataSource implements MovieDataSource{

    private static MoviesRemoteDataSource INSTANCE;
    private static String mMoviesData;
    private static Context mContext;
    private LoadMoviesCallback mMoviesCallBack;



    public static MoviesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private MoviesRemoteDataSource() {}

    /**
     * Builds the URL for popular movies which is used to talk to the moviedb server.
     *
     * @return The URL to use to query the weather server.
     */

    @Override
    public void getMovies(final String sortby, final int page, @NonNull final LoadMoviesCallback callback) {
        URL url = MovieResponseParser.buildURL(sortby, page);
        mMoviesCallBack = callback;
        new LoadMoviesTask().execute(url);
    }


    public void getReviewsTrailers(String movieId, @NonNull GetMovieCallback callback){
        URL url = MovieResponseParser.buildURLForReviewTrailers(movieId);
        MovieData reviewsTrailers = null;
        try {
            String jsonGenreResponse = MovieResponseParser.getResponseFromHttpUrl(url);
            reviewsTrailers = MovieResponseParser.getTrailerReviewsFromJson(jsonGenreResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(reviewsTrailers!=null){
            callback.onTaskLoaded(reviewsTrailers);
        }else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMovie(@NonNull String movieId, @NonNull GetMovieCallback callback) {

    }

    @Override
    public void insertMovie(@NonNull MovieData movieData) {

    }

    @Override
    public void deleteAllMovies() {

    }

    @Override
    public void deleteMovie(@NonNull String movieId) {

    }

    class LoadMoviesTask extends AsyncTask<URL, Void, ArrayList<MovieData>> {
        ArrayList<MovieData> movieDataList;

        @Override
        protected ArrayList<MovieData> doInBackground(URL... params) {
            try {
                mMoviesData = MovieResponseParser.getResponseFromHttpUrl(params[0]);
                movieDataList = MovieResponseParser.getSimpleMovieDataFromJson(mMoviesData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieData> movieDataList) {
            super.onPostExecute(movieDataList);
            if (movieDataList != null && movieDataList.size() != 0) {
                mMoviesCallBack.onMoviesLoaded(movieDataList);
            } else {
                mMoviesCallBack.onDataNotAvailable();
            }
        }
    }
}
