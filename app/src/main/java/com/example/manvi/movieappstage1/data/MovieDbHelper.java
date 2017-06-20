package com.example.manvi.movieappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by manvi on 15/3/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {


    private final static int DATABASE_VERSION =3;
    public final static String DATABASE_NAME = "movies.db";

    //Table for all the movies
    final String SQL_CREATE_MOVIE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.MovieDataEntry.TABLE_NAME +
                                        " (" + MovieContract.MovieDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_LANG + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_POPULARITY + " REAK NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_VOTE_AVG + " REAL NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_VOTE_COUNT+" INTEGER NOT NULL, " +
                                                MovieContract.MovieDataEntry.COLUMN_POPULAR+ " INTEGER NOT NULL, "+
                                                MovieContract.MovieDataEntry.COLUMN_TOP_RATED + " INTEGER NOT NULL, " +
                                                "UNIQUE (" + MovieContract.MovieDataEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE" +
                                        ");";

    //Table for the Favorite Movies
    final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME +
                                            " (" + MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID + " INTEGER NOT NULL, " +
                                                    MovieContract.FavoriteMovieEntry.COLUMN_CREATION_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                                                    "UNIQUE (" + MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID + ") ON CONFLICT REPLACE" +
                                                    " FOREIGN KEY ("+MovieContract.FavoriteMovieEntry.COLUMN_FAV_MOVIE_ID+") REFERENCES "+
                                                    MovieContract.MovieDataEntry.TABLE_NAME+"("+MovieContract.MovieDataEntry.COLUMN_MOVIE_ID+")" +
                                            ");";

    final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.GenreList.TABLE_NAME +
            " (" + MovieContract.GenreList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.GenreList.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
            MovieContract.GenreList.COLUMN_GENRE_NAME + " TEXT NOT NULL, " +
            "UNIQUE (" + MovieContract.GenreList.COLUMN_GENRE_ID + ") ON CONFLICT REPLACE"+
            ");";


    final String SQL_CREATE_GENRE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.movieGenreList.TABLE_NAME +
            " (" + MovieContract.movieGenreList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.movieGenreList.COLUMN_MOVIE_DB_ID + " INTEGER NOT NULL, " +
            MovieContract.movieGenreList.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
            " FOREIGN KEY ("+MovieContract.movieGenreList.COLUMN_MOVIE_DB_ID+") REFERENCES "+
            MovieContract.MovieDataEntry.TABLE_NAME+"("+MovieContract.MovieDataEntry.COLUMN_MOVIE_ID+")" +
            ");";


    final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.trailerList.TABLE_NAME +
            " (" + MovieContract.trailerList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.trailerList.COLUMN_TRAILER_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.trailerList.COLUMN_ID + " INTEGER NOT NULL, " +
            MovieContract.trailerList.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +
            MovieContract.trailerList.COLUMN_YOUTUBE_URL + " TEXT NOT NULL, " +
            "UNIQUE (" + MovieContract.trailerList.COLUMN_ID + ") ON CONFLICT REPLACE"+
            " FOREIGN KEY ("+MovieContract.trailerList.COLUMN_TRAILER_MOVIE_ID+") REFERENCES "+
            MovieContract.MovieDataEntry.TABLE_NAME+"("+MovieContract.MovieDataEntry.COLUMN_MOVIE_ID+")" +
            ");";


    final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieContract.reviewList.TABLE_NAME +
            " (" + MovieContract.reviewList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieContract.reviewList.COLUMN_REVIEW_MOVIE_ID + " INTEGER NOT NULL, " +
            MovieContract.reviewList.COLUMN_ID + " INTEGER NOT NULL, " +
            MovieContract.reviewList.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            MovieContract.reviewList.COLUMN_CONTENT + " TEXT NOT NULL, " +
            "UNIQUE (" + MovieContract.reviewList.COLUMN_ID + ") ON CONFLICT REPLACE"+
            " FOREIGN KEY ("+MovieContract.reviewList.COLUMN_REVIEW_MOVIE_ID+") REFERENCES "+
            MovieContract.MovieDataEntry.TABLE_NAME+"("+MovieContract.MovieDataEntry.COLUMN_MOVIE_ID+")" +
            ");";

    //default constructor
    public MovieDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //these methods takes care of opening the database if it exists, creating it if it does not, and upgrading it as necessary.
    // Transactions are used to make sure the database is always in a sensible state.

    //OnConfigure is called before onOpen, onCreate and onUpgrade functions.
    //refer: http://stackoverflow.com/questions/2545558/foreign-key-constraints-in-android-using-sqlite-on-delete-cascade
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if(!db.isReadOnly())
        {
            setForeignKeyConstraintsEnabled(db);
        }
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            //From API 16, this is used to enable the foreign key support.
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db)
    {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db)
    {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME,Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DATA_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDataEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.GenreList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.movieGenreList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.trailerList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.reviewList.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
