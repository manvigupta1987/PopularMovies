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
    private ArrayList<MovieData> results;

    public ArrayList<MovieData> getResults() {
        return this.results;
    }
}