package com.example.manvi.movieappstage1.data.Source.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by manvi on 15/3/17.
 */

public class MovieContract {

    MovieContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.manvi.movieappstage1";

    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE_MOVIE = "favourite_movie";

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIE;

        public static final String TABLE_NAME = "favorite_movies";

        //Movie ID from the json data. This will be act as foreign key for the other tables.
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "original_title";
        public static final String COLUMN_BACKDROP = "backdrop_path";
        public static final String COLUMN_LANG = "original_language";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVG = "average_vote";
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static Uri buildMovieDataUriWithMovieID(long movie_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movie_id)).build();
        }
    }
}

