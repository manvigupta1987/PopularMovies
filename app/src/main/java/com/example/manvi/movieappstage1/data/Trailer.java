package com.example.manvi.movieappstage1.data;

/**
 * Created by manvi on 20/3/17.
 */

public class Trailer {
    private String mId;
    private String mURL;
    private String mthumbnailUrl;

    public Trailer (String mId, String mURL, String mthumbnailUrl) {
        this.mId = mId;
        this.mURL = mURL;
        this.mthumbnailUrl = mthumbnailUrl;
    }

    public String getmId()
    {
        return this.mId;
    }
    public String getmURL(){
        return mURL;
    }

    public String getMthumbnailUrl() {
        return mthumbnailUrl;
    }
}
