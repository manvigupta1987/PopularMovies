package com.example.manvi.movieappstage1.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by manvi on 20/3/17.
 */

public class Reviews {

    @SerializedName("author")
    @Expose
    private String mAuthor;
    @SerializedName("content")
    @Expose
    private String mContent;

    public String getmAuthor(){
        return this.mAuthor;
    }

    public String getmContent()
    {
        return this.mContent;
    }
}
