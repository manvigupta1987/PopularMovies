package com.example.manvi.movieappstage1.data.Source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MovieDbHelper extends SQLiteOpenHelper {


    private final static int DATABASE_VERSION =1;
    private final static String DATABASE_NAME = "favourite_movies.db";

    //default constructor
    public MovieDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME +
                " (" + MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_LANG + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY + " REAK NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL, " +
                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                "UNIQUE (" + MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
