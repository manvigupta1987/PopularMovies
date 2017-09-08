package com.example.manvi.movieappstage1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.data.MovieContract;

/**
 * Created by manvi on 7/9/17.
 */

public class UpdateFavouriteMovieDBTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = UpdateFavouriteMovieDBTask.class.getSimpleName();

    private Context mContext;
    private MovieData mMovie;

    public UpdateFavouriteMovieDBTask(Context context, MovieData movie){
        mContext = context;
        mMovie = movie;
    }

    @Override
    protected Void doInBackground(Void... params) {
        deleteOrSaveFavoriteMovie();
        return null;
    }

    /**
     * Method to handle deletion of a favorite movie if it exists in the favourite movie database
     * or to insert it if doesn't exist
     */
    private void deleteOrSaveFavoriteMovie() {
        //Check if the movie with this movie_id  exists in the db
        Cursor favMovieCursor = mContext.getContentResolver().query(
                MovieContract.FavoriteMovieEntry.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID},
                MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(mMovie.getMovieID())},
                null);

        // If it exists, delete the movie with that movie id
        if (favMovieCursor.moveToFirst()) {
            Uri uri = MovieContract.FavoriteMovieEntry.buildMovieDataUriWithMovieID(mMovie.getMovieID());
            int rowDeleted = mContext.getContentResolver().delete(uri,
                    MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{String.valueOf(mMovie.getMovieID())});

        } else {
            // Otherwise, insert it using the content resolver and the base URI
            ContentValues values = new ContentValues();
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, mMovie.getMovieID());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP, mMovie.getFavBackDropPath(mContext));
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_LANG, mMovie.getOriginalLang());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY, mMovie.getPopularity());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, mMovie.getFavPoster_path(mContext));
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG, mMovie.getVoteAvgCount());
            values.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, mMovie.getVoteCount());

            // Finally, insert movie data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.FavoriteMovieEntry.CONTENT_URI,
                    values);
        }
        favMovieCursor.close();
    }
}
