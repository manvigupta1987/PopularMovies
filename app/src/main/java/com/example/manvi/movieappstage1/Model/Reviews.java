package com.example.manvi.movieappstage1.Model;

/**
 * Created by manvi on 20/3/17.
 */

public class Reviews {
    private String mId;
    private String mAuthor;
    private String mContent;

    public Reviews(String id, String Author, String Content )
    {
        this.mId = id;
        this.mAuthor = Author;
        this.mContent = Content;
    }

    public String getmId(){
        return this.mId;
    }
    public String getmAuthor(){
        return this.mAuthor;
    }

    public String getmContent()
    {
        return this.mContent;
    }
}
