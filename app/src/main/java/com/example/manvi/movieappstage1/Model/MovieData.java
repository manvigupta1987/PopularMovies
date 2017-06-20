package com.example.manvi.movieappstage1.Model;

import android.content.Context;
import android.content.res.Resources;

import com.example.manvi.movieappstage1.R;

import java.util.ArrayList;

/**
 * Created by manvi on 1/3/17.
 */

public class MovieData {

    private final String TAG = MovieData.class.getSimpleName();
    private long mMovieId;
    private String mBackDropPath;
    private String mOriginalLang;
    private String mPoster;
    private String mOverview;
    private String mReleaseDate;
    private String mTitle;
    private Double mVoteAverage;
    private Double mPopularity;
    private int mVoteCount;
    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Reviews> mReviewList;

    public MovieData(){};

    public MovieData(long MovieId,String BackDropPath,String OriginalLang,
                     String title, String poster_path, String overview,
                     String release_date, Double vote_count,Double Popularity,
                     int VoteCount)
    {
        this.mMovieId = MovieId;
        this.mBackDropPath = BackDropPath;
        this.mOriginalLang = OriginalLang;
        this.mTitle = title;
        this.mPoster = poster_path;
        this.mOverview = overview;
        this.mReleaseDate = release_date;
        this.mVoteAverage = vote_count;
        this.mPopularity = Popularity;
        this.mVoteCount = VoteCount;
    }

    public MovieData(ArrayList<Trailer> trailerList, ArrayList<Reviews> ReviewList ){
        this.mTrailerList = trailerList;
        this.mReviewList = ReviewList;
    }

    public long getMovieID()
    {
        return this.mMovieId;
    }

    public String getOriginalLang()
    {
        return this.mOriginalLang;
    }

    public Double getPopularity()
    {
        return this.mPopularity;
    }

    public int getVoteCount(){
        return this.mVoteCount;
    }

    public String getBackDropPath(Context context)
    {
        Resources res= context.getResources();
        if(mBackDropPath!=null && !mBackDropPath.equals(""))
        {
            String poster_path = res.getString(R.string.base_url_for_downloading_movie_poster) + res.getString(R.string.backDrop_poster_size) + mBackDropPath;
            return (poster_path);
        }
        else
        {
            return null;
        }
    }


    //returns the complete path of the image poster.
    public String getPoster_path(Context context)
    {

        Resources res= context.getResources();
        if(mPoster!=null && !mPoster.equals(""))
        {
            String poster_path = res.getString(R.string.base_url_for_downloading_movie_poster) + res.getString(R.string.movie_poster_size) + mPoster;
            return (poster_path);
        }
        else
        {
            return null;
        }
    }

    public String getTitle()
    {
        return this.mTitle;
    }

    public String getOverview(){
        return this.mOverview;
    }

    public String getReleaseDate()
    {
        return this.mReleaseDate;
    }

    public Double getVoteAvgCount()
    {
        return this.mVoteAverage;
    }

    public ArrayList<Trailer> getmTrailerList()
    {
        return this.mTrailerList;
    }

    public ArrayList<Reviews> getmReviewListList()
    {
        return this.mReviewList;
    }
}
