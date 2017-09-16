package com.example.manvi.movieappstage1.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by manvi on 14/9/17.
 */

public class MovieResponse {

    @SerializedName("results")
    @Expose
    private ArrayList<Movie> results;

    public ArrayList<Movie> getResults() {
        return this.results;
    }
}