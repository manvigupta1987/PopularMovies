package com.example.manvi.movieappstage1.Model;

/**
 * Created by manvi on 18/3/17.
 */

public class MovieGenre {
    private final static String TAG = MovieGenre.class.getSimpleName();

    private long mMovie_db_id;
    private long mGenreId;

    public MovieGenre(long Movie_db_id, long GenreId)
    {
        this.mMovie_db_id = Movie_db_id;
        this.mGenreId = GenreId;
    }

    public long getMovie_db_id(){
        return this.mMovie_db_id;
    }

    public long getGenreId()
    {
        return this.mGenreId;
    }
}

