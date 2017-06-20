package com.example.manvi.movieappstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.util.Log;


/**
 * Created by manvi on 15/3/17.
 */

public class MovieDataProvider extends ContentProvider {

    private static final int CODE_MOVIE_DIR = 100;
    private static final int CODE_MOVIE_ID = 101;
    private static final int CODE_MOVIE_STRING = 102;

    private static final int CODE_FAVOURITE_MOVIE_DIR = 200;
    private static final int CODE_FAVOURITE_MOVIE = 201;

    private static final int CODE_GENRE_DIR = 300;
    private static final int CODE_GENRE_ID = 301;

    private static final int CODE_MOVIE_GENRE_DIR = 400;
    private static final int CODE_MOVIE_GENRE_ID = 401;

    private static final int CODE_REVIEW_DIR = 500;
    private static final int CODE_REVIEW_MOVIE = 501;

    private static final int CODE_TRAILER_DIR = 600;
    private static final int CODE_TRAILER_MOVIE = 601;

    private static final String TAG = MovieDataProvider.class.getSimpleName();

    private MovieDbHelper mMovieDbHelper;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIES,CODE_MOVIE_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIES + "/#",CODE_MOVIE_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIES + "/*",CODE_MOVIE_STRING);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVOURITE_MOVIE,CODE_FAVOURITE_MOVIE_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVOURITE_MOVIE + "/#",CODE_FAVOURITE_MOVIE);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_GENRE,CODE_GENRE_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_GENRE + "/#",CODE_GENRE_ID);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_GENRE_MOVIE,CODE_MOVIE_GENRE_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_GENRE_MOVIE + "/#",CODE_MOVIE_GENRE_ID);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER,CODE_TRAILER_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER + "/#",CODE_TRAILER_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEWS,CODE_REVIEW_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEWS+"/#", CODE_REVIEW_MOVIE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        int matchId = sUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mMovieDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (matchId){
            case CODE_MOVIE_DIR:
                cursor = sqLiteDatabase.query(MovieContract.MovieDataEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,orderBy);
                break;
            case CODE_MOVIE_ID:
                String id = uri.getLastPathSegment();
                Log.e(TAG,"Movie ID = "+ id);
                selection = MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + " = ? ";
                selectionArgs = new String[]{id};

                String movieIdQuery = "SELECT " + MovieContract.MovieDataEntry.COLUMN_BACKDROP + " , " + MovieContract.MovieDataEntry.COLUMN_LANG + " , "+
                        MovieContract.MovieDataEntry.COLUMN_RELEASE_DATE + " , " + MovieContract.MovieDataEntry.COLUMN_POSTER_PATH + " , " +
                        MovieContract.MovieDataEntry.COLUMN_VOTE_AVG + " , " + MovieContract.MovieDataEntry.COLUMN_VOTE_COUNT + " , " +
                        MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + " , "+ MovieContract.MovieDataEntry.COLUMN_TITLE + " , " + MovieContract.MovieDataEntry.COLUMN_OVERVIEW +
                        " , group_concat(" + MovieContract.GenreList.TABLE_NAME + "."
                        + MovieContract.GenreList.COLUMN_GENRE_NAME + ", ', ' ) as name" +
                        " FROM " + MovieContract.MovieDataEntry.TABLE_NAME + " INNER JOIN " + MovieContract.movieGenreList.TABLE_NAME
                        + " ON " + MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_MOVIE_ID+
                        " = " + MovieContract.movieGenreList.TABLE_NAME + "." +MovieContract.movieGenreList.COLUMN_MOVIE_DB_ID +
                        " INNER JOIN " + MovieContract.GenreList.TABLE_NAME + " ON " + MovieContract.movieGenreList.TABLE_NAME + "."
                        +MovieContract.movieGenreList.COLUMN_GENRE_ID  + " = " + MovieContract.GenreList.TABLE_NAME + "." + MovieContract.GenreList.COLUMN_GENRE_ID +
                        " WHERE " + MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + " =?" +
                        " GROUP BY " + MovieContract.movieGenreList.COLUMN_MOVIE_DB_ID ;

                cursor = sqLiteDatabase.rawQuery(movieIdQuery,selectionArgs);
                break;

            case CODE_FAVOURITE_MOVIE_DIR:
                Log.e(TAG,"Movie Data Query");
                String rawQuery = "SELECT * FROM " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " INNER JOIN " + MovieContract.MovieDataEntry.TABLE_NAME
                        + " ON " + MovieContract.FavoriteMovieEntry.TABLE_NAME + "." + MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID+
                        " = " + MovieContract.MovieDataEntry.TABLE_NAME + "." +MovieContract.MovieDataEntry.COLUMN_MOVIE_ID +
                        " ORDER BY " + MovieContract.FavoriteMovieEntry.COLUMN_CREATION_TIME;

                cursor = sqLiteDatabase.rawQuery(rawQuery,null);
                break;

            case CODE_FAVOURITE_MOVIE:
                String Query = "SELECT * FROM " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " INNER JOIN " + MovieContract.MovieDataEntry.TABLE_NAME
                        + " ON " + MovieContract.FavoriteMovieEntry.TABLE_NAME + "." + MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID+
                        " = " + MovieContract.MovieDataEntry.TABLE_NAME + "." +MovieContract.MovieDataEntry.COLUMN_MOVIE_ID +
                        " WHERE " + MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID + " =? ";

                String movieId =  uri.getLastPathSegment();
                selectionArgs = new String[]{movieId};
                cursor = sqLiteDatabase.rawQuery(Query,selectionArgs);
                break;
            case CODE_GENRE_DIR:
                cursor = sqLiteDatabase.query(MovieContract.GenreList.TABLE_NAME,projection,selection,selectionArgs,null,null,orderBy);
                break;
            case CODE_GENRE_ID:
                String genre_id = uri.getLastPathSegment();
                selection = MovieContract.GenreList.COLUMN_GENRE_ID + " =? ";
                selectionArgs = new String[]{genre_id};
                cursor = sqLiteDatabase.query(MovieContract.GenreList.TABLE_NAME,projection,selection,selectionArgs,null,null,orderBy);
                break;
            case CODE_TRAILER_MOVIE:
                String trailerMovieId = uri.getLastPathSegment();
                selection = MovieContract.trailerList.COLUMN_TRAILER_MOVIE_ID + " =? ";
                selectionArgs = new String[]{trailerMovieId};

                cursor = sqLiteDatabase.query(MovieContract.trailerList.TABLE_NAME, projection, selection, selectionArgs, null, null,null);
                break;

            case CODE_REVIEW_MOVIE:
                String reviewMovieId = uri.getLastPathSegment();
                selection = MovieContract.reviewList.COLUMN_REVIEW_MOVIE_ID + " =? ";
                selectionArgs = new String[]{reviewMovieId};

                cursor = sqLiteDatabase.query(MovieContract.reviewList.TABLE_NAME, projection, selection, selectionArgs, null, null,null);
                break;
            default:
                throw new IllegalArgumentException("Invaild URI exception");
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int matchId = sUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mMovieDbHelper.getWritableDatabase();
        switch (matchId){
            case CODE_MOVIE_DIR: {
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieContract.MovieDataEntry.TABLE_NAME, null, value);
                        if (_id > 0) {
                            rowInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_GENRE_DIR:
            {
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieContract.GenreList.TABLE_NAME, null, value);
                        if (_id > 0) {
                            rowInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_MOVIE_GENRE_DIR:
            {
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieContract.movieGenreList.TABLE_NAME, null, value);
                        if (_id > 0) {
                            rowInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_REVIEW_DIR:
            {
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieContract.reviewList.TABLE_NAME, null, value);
                        if (_id > 0) {
                            rowInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            case CODE_TRAILER_DIR:
            {
                sqLiteDatabase.beginTransaction();
                int rowInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(MovieContract.trailerList.TABLE_NAME, null, value);
                        if (_id > 0) {
                            rowInserted++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int matchId = sUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mMovieDbHelper.getWritableDatabase();
        long rowInserted;
        switch (matchId) {
            case CODE_FAVOURITE_MOVIE_DIR:
                rowInserted = sqLiteDatabase.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowInserted >0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
            return ContentUris.withAppendedId(uri,rowInserted);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int matchId = sUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mMovieDbHelper.getWritableDatabase();
        int rowDeleted;
        switch (matchId)
        {
            case CODE_FAVOURITE_MOVIE:
            {
                String id = uri.getLastPathSegment();
                selection = MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID + " =? ";
                selectionArgs = new String[]{id};
                rowDeleted = sqLiteDatabase.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowDeleted > 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int matchId = sUriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mMovieDbHelper.getWritableDatabase();
        int rowUpdated;
        switch (matchId) {
            case CODE_MOVIE_ID: {
                String id = uri.getLastPathSegment();
                selection = MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{id};
                rowUpdated = sqLiteDatabase.update(MovieContract.MovieDataEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowUpdated >0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdated;
    }
}
