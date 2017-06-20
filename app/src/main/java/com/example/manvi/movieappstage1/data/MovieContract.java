package com.example.manvi.movieappstage1.data;

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

    public static final String PATH_MOVIES = "movie";
    public static final String PATH_FAVOURITE_MOVIE = "favourite_movie";
    public static final String PATH_GENRE_MOVIE = "genre_movie";
    public static final String PATH_GENRE = "genre";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEWS = "reviews";


    public static final class MovieDataEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "moviedata";

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
        public static final String COLUMN_TOP_RATED = "top_rated";
        public static final String COLUMN_POPULAR = "popular";


        /**
         * Returns just the selection part of the movie query based on popular/top-rated/favourite.
         *
         * @return The selection part of the movie query
         */
        public static String getSqlSelectPopularMovieCateogry() {
            return MovieDataEntry.COLUMN_POPULAR + " = ? ";
        }

        public static String getSqlSelectTopRatedMovieCateogry() {
            return MovieDataEntry.COLUMN_TOP_RATED + " = ? ";
        }

        public static Uri buildMovieDataUriWithMovieID(long movie_id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movie_id)).build();
        }
    }


    public static final class FavoriteMovieEntry implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_FAVOURITE_MOVIE).build();
        public static final String TABLE_NAME = "favorite_movies";

        //Movie ID from the json data. This will be act as foreign key for the other tables.
        public static final String COLUMN_FAV_MOVIE_ID = "fav_movie_id";
        public static final String COLUMN_CREATION_TIME = "time";
    }

    public static final class GenreList implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_GENRE).build();
        public static final String TABLE_NAME = "genrelist";

        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_GENRE_NAME = "genre_name";
    }

    public static final class movieGenreList implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_GENRE_MOVIE).build();
        public static final String TABLE_NAME = "moviegenrelist";

        public static final String COLUMN_MOVIE_DB_ID = "movie_db_id";
        public static final String COLUMN_GENRE_ID = "genre_id";

    }

    public static final class trailerList implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_TRAILER).build();
        public static final String TABLE_NAME = "trailerList";

        public static final String COLUMN_TRAILER_MOVIE_ID = "trailer_movie_id";
        public static final String COLUMN_ID = "trailer_id";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_YOUTUBE_URL = "url";
    }

    public static final class reviewList implements BaseColumns {

        public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String TABLE_NAME = "reviewList";

        public static final String COLUMN_REVIEW_MOVIE_ID = "review_movie_id";
        public static final String COLUMN_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }
}

