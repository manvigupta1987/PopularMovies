package com.entertainment.manvi.MyMovies.data.Source.local;

import android.provider.BaseColumns;

class MovieContract {

    MovieContract() {
    }

    public static final class FavoriteMovieEntry implements BaseColumns {

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
    }
}

