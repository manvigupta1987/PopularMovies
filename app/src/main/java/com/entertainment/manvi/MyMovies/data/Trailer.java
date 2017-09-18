package com.entertainment.manvi.MyMovies.data;

import com.entertainment.manvi.MyMovies.Utils.NetworkUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailer {

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    @SerializedName("id")
    @Expose
    private String mId;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("site")
    @Expose
    private String site;

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getVideoUrl() {
        if (this.site.equals("YouTube")) {
            return YOUTUBE_URL + getKey();
        }
        return null;
    }

    public String getThumbNailUrl(){
        return NetworkUtils.buildYouTubeThumbNailURLForTrailers(this.key);
    }
}

