package com.entertainment.manvi.MyMovies.data.Source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.entertainment.manvi.MyMovies.Utils.schedulers.BaseSchedulerProvider;
import com.entertainment.manvi.MyMovies.data.Movie;
import com.entertainment.manvi.MyMovies.data.Reviews;
import com.entertainment.manvi.MyMovies.data.Source.MovieDataSource;
import com.entertainment.manvi.MyMovies.data.Trailer;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;

public class MoviesLocalDataSource implements MovieDataSource {

    private static MoviesLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    private MoviesLocalDataSource(@NonNull Context context,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context);
        checkNotNull(schedulerProvider);

        MovieDbHelper mDbHelper = new MovieDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mDbHelper, schedulerProvider.io());
    }


    private static final Func1<SqlBrite.Query, List<Movie>> QUERY_TO_LIST_MAPPER =
            query -> {
                Cursor cursor = query.run();
                    try {
                        if(cursor!=null) {
                            List<Movie> movies = new ArrayList<>(cursor.getCount());
                            while (cursor.moveToNext()) {
                                Movie movie = createFromCursor(cursor);
                                movies.add(movie);
                            }
                            return movies;
                        }else {
                            return null;
                        }
                    } finally {
                        if(cursor!=null) {
                            cursor.close();
                        }
                    }
            };

    private static final Func1<SqlBrite.Query, Movie> QUERY_TO_ITEM_MAPPER = query -> {
        Cursor cursor = query.run();
        try {
            if(cursor!=null) {
                cursor.moveToNext();
                return createFromCursor(cursor);
            }
            return null;
        } finally {
            if(cursor!=null) {
                cursor.close();
            }
        }
    };

    @NonNull
    private static Movie createFromCursor(@NonNull Cursor cursor) {
        return new Movie(cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_LANG)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)),
                cursor.getDouble(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG)),
                cursor.getDouble(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY)),
                cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT)));
    }


    public static MoviesLocalDataSource getInstance(@NonNull Context context,
                                                    @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<Movie>> getMovies(String sortBy, int page) {

        return mDatabaseHelper
                .createQuery(MovieContract.FavoriteMovieEntry.TABLE_NAME, "SELECT * FROM " +MovieContract.FavoriteMovieEntry.TABLE_NAME)
                .map(QUERY_TO_LIST_MAPPER);
    }

    @Override
    public Observable<Movie> getMovie(@NonNull String movieId) {

        return mDatabaseHelper
                .createQuery(MovieContract.FavoriteMovieEntry.TABLE_NAME, "SELECT * FROM " +MovieContract.FavoriteMovieEntry.TABLE_NAME + " WHERE " + MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID  + " = ?", String.valueOf(movieId))
                .map(QUERY_TO_ITEM_MAPPER);
    }

    @Override
    public void insertMovie(@NonNull Movie movie) {
        checkNotNull(movie);
        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getMovieID());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP, movie.getFavBackDropPath());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_LANG, movie.getOriginalLang());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.getFavPoster_path());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG, movie.getVoteAvgCount());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());

        mDatabaseHelper.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteMovie(@NonNull Movie movie) {
        Long movieId = movie.getMovieID();
        String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " =? ";
        String[] selectionArgs = { movieId.toString() };
        mDatabaseHelper.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public Observable<List<Reviews>> getReviews(Long movieId) {
        return null;
    }

    @Override
    public Observable<List<Trailer>> getTrailer(Long movieId) {
        return null;
    }
}

