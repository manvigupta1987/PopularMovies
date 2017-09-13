package com.example.manvi.movieappstage1.data.Source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by manvi on 15/3/17.
 */

public class MoviesLocalDataSource implements MovieDataSource {

    private static MoviesLocalDataSource INSTANCE;

    private MovieDbHelper mDbHelper;
    private Context mContext;
    private LoadMoviesCallback mMoviesCallBack;

    // Prevent direct instantiation.
    private MoviesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new MovieDbHelper(context);
        mContext = context;
    }

    public static MoviesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MoviesLocalDataSource(context);
        }
        return INSTANCE;
    }



    @Override
    public void getMovies(String sortBy, int page, @NonNull LoadMoviesCallback callback) {
        mMoviesCallBack = callback;
        new LoadFavoriteMoviesTask().execute();
    }

    @Override
    public void getMovie(@NonNull String movieId, @NonNull GetMovieCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                MovieContract.FavoriteMovieEntry.COLUMN_TITLE,
                MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP,
                MovieContract.FavoriteMovieEntry.COLUMN_LANG,
                MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
                MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY,
                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG };

        String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " =? ";
        String[] selectionArgs = { movieId };

        Cursor cursor = db.query(
                MovieContract.FavoriteMovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        MovieData movieData = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            movieData = new MovieData(cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)),
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
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        if (movieData != null) {
            callback.onTaskLoaded(movieData);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void insertMovie(@NonNull MovieData movieData) {
        checkNotNull(movieData);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movieData.getMovieID());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP, movieData.getFavBackDropPath(mContext));
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_LANG, movieData.getOriginalLang());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, movieData.getOverview());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY, movieData.getPopularity());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, movieData.getFavPoster_path(mContext));
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, movieData.getTitle());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG, movieData.getVoteAvgCount());
        values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, movieData.getVoteCount());

        db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void deleteAllMovies() {

    }

    @Override
    public void deleteMovie(@NonNull String movieId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " =? ";
        String[] selectionArgs = { movieId };

        db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void getReviewsTrailers(String movieId, @NonNull GetMovieCallback callback) {

    }

    private class LoadFavoriteMoviesTask extends AsyncTask<Void, Integer, ArrayList<MovieData>> {
        @Override
        protected ArrayList<MovieData> doInBackground(Void... params) {
            // Retrieve movie records from fav movie table
            //mDatasetList.clear();
            ArrayList<MovieData> movieList = new ArrayList<MovieData>();
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.FavoriteMovieEntry.COLUMN_TITLE,
                    MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP,
                    MovieContract.FavoriteMovieEntry.COLUMN_LANG,
                    MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
                    MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                    MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
                    MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY,
                    MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG,
                    MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT};

            Cursor cursor = db.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,
                    projection, null, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
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
                    movieList.add(movie);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieData> datalist) {
            super.onPostExecute(datalist);
            if (datalist.isEmpty()) {
                // This will be called if the table is new or just empty.
                mMoviesCallBack.onDataNotAvailable();
            } else {
                mMoviesCallBack.onMoviesLoaded(datalist);
            }
        }
    }
}

