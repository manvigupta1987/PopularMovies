package com.example.manvi.movieappstage1.Model;

import com.example.manvi.movieappstage1.data.MovieContract;

/**
 * Created by manvi on 18/3/17.
 */

public class GenreData {

    private final static String TAG = GenreData.class.getSimpleName();

    private long mGenreId;
    private String mGenreName;

    public GenreData(long genreId, String genreName)
    {
        this.mGenreId = genreId;
        this.mGenreName = genreName;
    }

    public long getGenreId()
    {
        return this.mGenreId;
    }

    public String getGenreName(){
        return this.mGenreName;
    }
}
