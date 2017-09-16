package com.example.manvi.movieappstage1.data.Source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.Utils.schedulers.BaseSchedulerProvider;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;
import com.example.manvi.movieappstage1.data.Trailer;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by manvi on 15/3/17.
 */

public class MoviesLocalDataSource implements MovieDataSource {

    private static MoviesLocalDataSource INSTANCE;

    private MovieDbHelper mDbHelper;
    private SqlBrite sqlBrite;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    private MoviesLocalDataSource(@NonNull Context context,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context);
        checkNotNull(schedulerProvider);

        mDbHelper = new MovieDbHelper(context);
        sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mDbHelper, schedulerProvider.io());
    }


    public static Func1<SqlBrite.Query, List<MovieData>> QUERY_TO_LIST_MAPPER =
            new Func1<SqlBrite.Query, List<MovieData>>() {
        @Override
        public List<MovieData> call(SqlBrite.Query query) {
            Cursor cursor = query.run();
            try {
                List<MovieData> movies = new ArrayList<MovieData>(cursor.getCount());
                while (cursor.moveToNext()) {
                    MovieData movie = createFromCursor(cursor);
                    movies.add(movie);
                }
                return movies;
            } finally {
                cursor.close();
            }
        }
    };

    public static Func1<SqlBrite.Query, MovieData> QUERY_TO_ITEM_MAPPER = new Func1<SqlBrite.Query, MovieData>() {
        @Override
        public MovieData call(SqlBrite.Query query) {
            Cursor cursor = query.run();
            try {
                cursor.moveToNext();
                return createFromCursor(cursor);
            } finally {
                cursor.close();
            }
        }
    };


    @NonNull
    private static MovieData createFromCursor(@NonNull Cursor cursor) {
        MovieData movie = new MovieData(cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_LANG)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)),
                cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)),
                cursor.getDouble(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG)),
                cursor.getDouble(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY)),
                cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT)));
        return movie;
    }


    public static MoviesLocalDataSource getInstance(@NonNull Context context,
                                                    @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }



    @Override
    public Observable<List<MovieData>> getMovies(String sortBy, int page) {
        Observable<List<MovieData>> selectedMovieObservable = mDatabaseHelper
                .createQuery(MovieContract.FavoriteMovieEntry.TABLE_NAME, "SELECT * FROM " +MovieContract.FavoriteMovieEntry.TABLE_NAME)
                .map(QUERY_TO_LIST_MAPPER);

        return selectedMovieObservable;
    }

    @Override
    public Observable<MovieData> getMovie(@NonNull String movieId) {

        Observable<MovieData> selectedMovieObservable = mDatabaseHelper
                .createQuery(MovieContract.FavoriteMovieEntry.TABLE_NAME, "SELECT * FROM " +MovieContract.FavoriteMovieEntry.TABLE_NAME + " WHERE " + MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID  + " = ?", String.valueOf(movieId))
                .map(QUERY_TO_ITEM_MAPPER);

        return  selectedMovieObservable;
    }

    @Override
    public void insertMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movieData.getMovieID());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP, movieData.getFavBackDropPath());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_LANG, movieData.getOriginalLang());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movieData.getOverview());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY, movieData.getPopularity());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, movieData.getFavPoster_path());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, movieData.getTitle());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG, movieData.getVoteAvgCount());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, movieData.getVoteCount());

        mDatabaseHelper.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteMovie(@NonNull MovieData movieData) {
        Long movieId = movieData.getMovieID();
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

