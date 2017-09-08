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

    private static final int CODE_FAVOURITE_MOVIE_DIR = 200;
    private static final int CODE_FAVOURITE_MOVIE = 201;

    private static final String TAG = MovieDataProvider.class.getSimpleName();

    private MovieDbHelper mMovieDbHelper;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVOURITE_MOVIE,CODE_FAVOURITE_MOVIE_DIR);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVOURITE_MOVIE + "/#",CODE_FAVOURITE_MOVIE);
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
            case CODE_FAVOURITE_MOVIE_DIR:
                cursor = sqLiteDatabase.query(
                    MovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;

            case CODE_FAVOURITE_MOVIE:
                String movieId = uri.getLastPathSegment();
                selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{movieId};
                cursor = sqLiteDatabase.query(MovieContract.FavoriteMovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,orderBy);
                break;
            default:
                throw new IllegalArgumentException("Invaild URI exception");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            /**
             * Get all favourite movie records
             */
            case CODE_FAVOURITE_MOVIE_DIR:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            /**
             * Get a movie record
             */
            case CODE_FAVOURITE_MOVIE:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_FAVOURITE_MOVIE_DIR: {
                long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.FavoriteMovieEntry.buildMovieDataUriWithMovieID(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //calling notifyChange on the Content Resolver
        //to notify all of the registered observers.
        //Note : We must use the passed in uri & not the returnUri,
        //as that will not correctly notify our cursors of the same.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
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
                selection = MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " =? ";
                selectionArgs = new String[]{id};
                rowDeleted = sqLiteDatabase.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowDeleted != 0)
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
            case CODE_FAVOURITE_MOVIE_DIR: {
                rowUpdated = sqLiteDatabase.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
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
