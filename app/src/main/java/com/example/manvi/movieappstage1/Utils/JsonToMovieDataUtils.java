package com.example.manvi.movieappstage1.Utils;

import android.content.ContentValues;
import android.content.Context;

import com.example.manvi.movieappstage1.Model.GenreData;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Model.MovieGenre;
import com.example.manvi.movieappstage1.Model.Reviews;
import com.example.manvi.movieappstage1.Model.Trailer;
import com.example.manvi.movieappstage1.data.MovieContract;

import java.util.ArrayList;

/**
 * Created by manvi on 16/3/17.
 */

public class JsonToMovieDataUtils {
    public static final String TAG = JsonToMovieDataUtils.class.getSimpleName();

    /*
    this function converts the movie data json response string to Content value.
     */
    public static ContentValues[] convertJsonDataToContentValue(Context context, ArrayList<MovieData> movieData, String movieGroup)
    {
        ContentValues[] values = new ContentValues[movieData.size()];
        int popular = 0;
        int topRated = 0;
        if(movieGroup.equals(ConstantsUtils.POPULAR_MOVIE))
        {
            popular = 1;
        }
        else
        {
            topRated =1;
        }
        for(int i =0; i<movieData.size();i++)
        {
            ContentValues value = new ContentValues();
            value.put(MovieContract.MovieDataEntry.COLUMN_MOVIE_ID,movieData.get(i).getMovieID());
            value.put(MovieContract.MovieDataEntry.COLUMN_BACKDROP,movieData.get(i).getBackDropPath(context));
            value.put(MovieContract.MovieDataEntry.COLUMN_LANG,movieData.get(i).getOriginalLang());
            value.put(MovieContract.MovieDataEntry.COLUMN_OVERVIEW,movieData.get(i).getOverview());
            value.put(MovieContract.MovieDataEntry.COLUMN_POPULARITY,movieData.get(i).getPopularity());
            value.put(MovieContract.MovieDataEntry.COLUMN_POSTER_PATH,movieData.get(i).getPoster_path(context));
            value.put(MovieContract.MovieDataEntry.COLUMN_RELEASE_DATE,movieData.get(i).getReleaseDate());
            value.put(MovieContract.MovieDataEntry.COLUMN_TITLE,movieData.get(i).getTitle());
            value.put(MovieContract.MovieDataEntry.COLUMN_VOTE_AVG,movieData.get(i).getVoteAvgCount());
            value.put(MovieContract.MovieDataEntry.COLUMN_VOTE_COUNT,movieData.get(i).getVoteCount());
            value.put(MovieContract.MovieDataEntry.COLUMN_POPULAR,popular);
            value.put(MovieContract.MovieDataEntry.COLUMN_TOP_RATED,topRated);
            values[i] = value;
        }
        return values;
    }

    /*
    this function converts the genre json response string to Content value.
     */
    public static ContentValues[] convertJsonGenreDataToContentValue(ArrayList<GenreData> genreData)
    {
        ContentValues[] values;
        if(genreData !=null) {
            values = new ContentValues[genreData.size()];
            for (int i = 0; i < genreData.size(); i++) {
                ContentValues value = new ContentValues();
                value.put(MovieContract.GenreList.COLUMN_GENRE_ID, genreData.get(i).getGenreId());
                value.put(MovieContract.GenreList.COLUMN_GENRE_NAME, genreData.get(i).getGenreName());
                values[i] = value;
            }
            return values;
        }
        else
        {
            return null;
        }
    }

    /*
    this function converts the movie genre json response string to Content value.
     */
    public static ContentValues[] convertJsonMovieGenreToContentValue(ArrayList<MovieGenre> movieGenre)
    {
        ContentValues[] values;
        if(movieGenre !=null) {
            values = new ContentValues[movieGenre.size()];
            for (int i = 0; i < movieGenre.size(); i++) {
                ContentValues value = new ContentValues();
                value.put(MovieContract.movieGenreList.COLUMN_MOVIE_DB_ID, movieGenre.get(i).getMovie_db_id());
                value.put(MovieContract.movieGenreList.COLUMN_GENRE_ID, movieGenre.get(i).getGenreId());
                values[i] = value;
            }
            return values;
        }
        else
        {
            return null;
        }
    }

    /*
    this function converts the trailer json response string to Content value.
     */
    public static ContentValues[] convertJsonTrailerListToContentValue(ArrayList<Trailer> trailerList, long movieId)
    {
        ContentValues[] values;
        if(trailerList !=null) {
            values = new ContentValues[trailerList.size()];
            for (int i = 0; i < trailerList.size(); i++) {
                ContentValues value = new ContentValues();
                value.put(MovieContract.trailerList.COLUMN_TRAILER_MOVIE_ID, movieId);
                value.put(MovieContract.trailerList.COLUMN_ID,trailerList.get(i).getmId());
                value.put(MovieContract.trailerList.COLUMN_THUMBNAIL_URL,trailerList.get(i).getMthumbnailUrl());
                value.put(MovieContract.trailerList.COLUMN_YOUTUBE_URL,trailerList.get(i).getmURL());
                values[i] = value;
            }
            return values;
        }
        else
        {
            return null;
        }
    }

    /*
    this function converts the review json response string to Content value.
     */
    public static ContentValues[] convertJsonReviewListToContentValue(ArrayList<Reviews> reviewList, long movieId)
    {
        ContentValues[] values;
        if(reviewList !=null) {
            values = new ContentValues[reviewList.size()];
            for (int i = 0; i < reviewList.size(); i++) {
                ContentValues value = new ContentValues();
                value.put(MovieContract.reviewList.COLUMN_REVIEW_MOVIE_ID, movieId);
                value.put(MovieContract.reviewList.COLUMN_AUTHOR,reviewList.get(i).getmAuthor());
                value.put(MovieContract.reviewList.COLUMN_CONTENT, reviewList.get(i).getmContent());
                value.put(MovieContract.reviewList.COLUMN_ID,reviewList.get(i).getmId());
                values[i] = value;
            }
            return values;
        }
        else
        {
            return null;
        }
    }
 }
