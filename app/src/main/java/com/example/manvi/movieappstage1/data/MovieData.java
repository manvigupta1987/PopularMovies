package com.example.manvi.movieappstage1.data;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.manvi.movieappstage1.R;

import java.util.ArrayList;

/**
 * Created by manvi on 1/3/17.
 */

public class MovieData implements Parcelable {

    private final String TAG = MovieData.class.getSimpleName();

    private int mMovieId;
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


    public MovieData(int MovieId,String BackDropPath,String OriginalLang,
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

    public MovieData(Parcel source) {

        this.mMovieId = source.readInt();
        this.mBackDropPath = source.readString();
        this.mOriginalLang = source.readString();
        this.mTitle = source.readString();
        this.mPoster = source.readString();
        this.mOverview = source.readString();
        this.mReleaseDate = source.readString();
        this.mVoteAverage = source.readDouble();
        this.mPopularity = source.readDouble();
        this.mVoteCount = source.readInt();
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

    public String getFavPoster_path(Context context)
    {
        Resources res= context.getResources();
        if(mPoster!=null && !mPoster.equals(""))
        {
            return mPoster;
        }
        else
        {
            return null;
        }
    }

    public String getFavBackDropPath(Context context)
    {
        Resources res= context.getResources();
        if(mBackDropPath!=null && !mBackDropPath.equals(""))
        {
            return (mBackDropPath);
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

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        public MovieData createFromParcel(Parcel source) {
            MovieData movie = new MovieData(source);
            return movie;
        }

        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mBackDropPath);
        dest.writeString(mOriginalLang);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
        dest.writeDouble(mPopularity);
        dest.writeInt(mVoteCount);
    }

    @Override
    public String toString() {
        return "MovieData{" +
                "mMovieId=" + mMovieId +
                ", mBackDropPath='" + mBackDropPath + '\'' +
                ", mOriginalLang='" + mOriginalLang + '\'' +
                ", mPoster='" + mPoster + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mVoteAverage=" + mVoteAverage +
                ", mPopularity=" + mPopularity +
                ", mVoteCount=" + mVoteCount +
                '}';
    }
}
