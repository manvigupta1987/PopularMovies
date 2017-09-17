package com.example.manvi.movieappstage1.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class MovieResponse {

    @SerializedName("results")
    @Expose
    private ArrayList<Movie> results;

    public ArrayList<Movie> getResults() {
        return this.results;
    }
}