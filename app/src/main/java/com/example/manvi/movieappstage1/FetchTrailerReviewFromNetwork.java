package com.example.manvi.movieappstage1;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;


import java.util.ArrayList;

/**
 * Created by manvi on 20/3/17.
 */

public class FetchTrailerReviewFromNetwork extends AsyncTask<Uri, Void, MovieData> {

    private final static String TAG = FetchTrailerReviewFromNetwork.class.getSimpleName();
    private Context mContext;
    private PostAsyncListener mPostAsyncListener;


    public FetchTrailerReviewFromNetwork(Context context, PostAsyncListener postAsyncListener) {
        mContext = context;
        this.mPostAsyncListener = postAsyncListener;
    }

    public interface PostAsyncListener {
        public void onPostExecuteListener(Context context, MovieData movieData);
    }

    @Override
    protected MovieData doInBackground(Uri... uris) {

        Uri uri = uris[0];
        String movieId = uri.getLastPathSegment();
        MovieData movieData = NetworkUtils.fetchReviewsTrailersFromNetwork(movieId);
        return movieData;
    }

    @Override
    protected void onPostExecute(MovieData movieData) {
        super.onPostExecute(movieData);
        mPostAsyncListener.onPostExecuteListener(mContext, movieData);
    }
}
