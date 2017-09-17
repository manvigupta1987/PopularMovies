package com.example.manvi.movieappstage1.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    private final String BASE_URL = "http://image.tmdb.org/t/p/";

    @SerializedName("id")
    @Expose
    private int mMovieId;
    @SerializedName("backdrop_path")
    @Expose
    private String mBackDropPath;
    @SerializedName("original_language")
    @Expose
    private String mOriginalLang;
    @SerializedName("poster_path")
    @Expose
    private String mPoster;
    @SerializedName("overview")
    @Expose
    private String mOverview;
    @SerializedName("release_date")
    @Expose
    private String mReleaseDate;
    @SerializedName("original_title")
    @Expose
    private String mTitle;
    @SerializedName("vote_average")
    @Expose
    private Double mVoteAverage;
    @SerializedName("popularity")
    @Expose
    private Double mPopularity;
    @SerializedName("vote_count")
    @Expose
    private final int mVoteCount;

    public Movie(int MovieId, String BackDropPath, String OriginalLang,
                 String title, String poster_path, String overview,
                 String release_date, Double vote_count, Double Popularity,
                 int VoteCount) {
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

    private Movie(Parcel source) {

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

    public long getMovieID() {
        return this.mMovieId;
    }

    public String getOriginalLang() {
        return this.mOriginalLang;
    }

    public Double getPopularity() {
        return this.mPopularity;
    }

    public int getVoteCount() {
        return this.mVoteCount;
    }

    public String getBackDropPath() {
        if (mBackDropPath != null && !mBackDropPath.equals("")) {
            String BACKDROP_SIZE = "w342";
            return (BASE_URL + BACKDROP_SIZE + mBackDropPath);
        } else {
            return null;
        }
    }


    //returns the complete path of the image poster.
    public String getPoster_path() {
        if (mPoster != null && !mPoster.equals("")) {
            String POSTER_SIZE = "w185";
            return (BASE_URL + POSTER_SIZE + mPoster);
        } else {
            return null;
        }
    }

    public String getFavPoster_path() {
        if (mPoster != null && !mPoster.equals("")) {
            return mPoster;
        } else {
            return null;
        }
    }

    public String getFavBackDropPath() {
        if (mBackDropPath != null && !mBackDropPath.equals("")) {
            return (mBackDropPath);
        } else {
            return null;
        }
    }


    public String getTitle() {
        return this.mTitle;
    }

    public String getOverview() {
        return this.mOverview;
    }

    public String getReleaseDate() {
        return this.mReleaseDate;
    }

    public Double getVoteAvgCount() {
        return this.mVoteAverage;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
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
        return "Movie{" +
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
