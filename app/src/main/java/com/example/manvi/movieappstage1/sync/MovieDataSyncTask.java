package com.example.manvi.movieappstage1.sync;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.manvi.movieappstage1.Model.GenreData;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Model.MovieGenre;
import com.example.manvi.movieappstage1.Utils.JsonToMovieDataUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieContract;


import java.util.ArrayList;

/**
 * Created by manvi on 15/3/17.
 */

public class MovieDataSyncTask {

    final static String POPULAR_MOVIE = "popular";
    final static String TOP_RATED_MOVIE = "top_rated";
    private final static String TAG = MovieDataSyncTask.class.getSimpleName();

    synchronized public static void syncMovieData(Context context){

        ArrayList<MovieData> popMovieDatas = NetworkUtils.fetchMovieInfoFromNetwork(POPULAR_MOVIE);
        if(popMovieDatas!=null) {
            ContentValues[] PopMovieContentvalues = JsonToMovieDataUtils.convertJsonDataToContentValue(context, popMovieDatas, POPULAR_MOVIE);
            if (PopMovieContentvalues != null && PopMovieContentvalues.length != 0) {
                context.getContentResolver().bulkInsert(MovieContract.MovieDataEntry.CONTENT_URI, PopMovieContentvalues);
            }
        }

        ArrayList<MovieGenre> popMovieGenre = NetworkUtils.extractMovieGenreFromNetwork();
        if(popMovieGenre!=null) {
            ContentValues[] popMovieGenreContentValues = JsonToMovieDataUtils.convertJsonMovieGenreToContentValue(popMovieGenre);
            if (popMovieGenreContentValues != null && popMovieGenreContentValues.length != 0) {
                context.getContentResolver().bulkInsert(MovieContract.movieGenreList.CONTENT_URI, popMovieGenreContentValues);
            }
        }

        ArrayList<MovieData> topRatedMovieDatas = NetworkUtils.fetchMovieInfoFromNetwork(TOP_RATED_MOVIE);
        if(topRatedMovieDatas!=null) {
            ContentValues[] topRatedContentValues = JsonToMovieDataUtils.convertJsonDataToContentValue(context, topRatedMovieDatas, TOP_RATED_MOVIE);
            if (topRatedContentValues != null && topRatedContentValues.length != 0) {
                context.getContentResolver().bulkInsert(MovieContract.MovieDataEntry.CONTENT_URI, topRatedContentValues);
            }
        }

        ArrayList<MovieGenre> topratedMovieGenre = NetworkUtils.extractMovieGenreFromNetwork();
        if(topratedMovieGenre!=null) {
            ContentValues[] topratedMovieGenreContentValues = JsonToMovieDataUtils.convertJsonMovieGenreToContentValue(topratedMovieGenre);
            if (topratedMovieGenreContentValues != null && topratedMovieGenreContentValues.length != 0) {
                context.getContentResolver().bulkInsert(MovieContract.movieGenreList.CONTENT_URI, topratedMovieGenreContentValues);
            }
        }

        ArrayList<GenreData> genreDatas = NetworkUtils.fetchGenreListFromNetwork();
        if(genreDatas!=null) {
            ContentValues[] genreDataContentValues = JsonToMovieDataUtils.convertJsonGenreDataToContentValue(genreDatas);
            if (genreDataContentValues != null && genreDataContentValues.length != 0) {
                context.getContentResolver().bulkInsert(MovieContract.GenreList.CONTENT_URI, genreDataContentValues);
            }
        }
    }
}
