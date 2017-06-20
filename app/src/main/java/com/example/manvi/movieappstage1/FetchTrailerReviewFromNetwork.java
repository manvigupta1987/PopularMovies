package com.example.manvi.movieappstage1;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.manvi.movieappstage1.Adapter.TrailerAdapter;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Model.Reviews;
import com.example.manvi.movieappstage1.Model.Trailer;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.JsonToMovieDataUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieContract;
import com.example.manvi.movieappstage1.sync.MovieDataSyncUtils;

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
        Long selMovieId = Long.parseLong(movieId);

        //if network connection is not present, query from the dataBase and if trailers and reviews are available in the database,
        //show it to the user.
        if (!NetworkUtils.isNetworkConnectionAvailable(mContext))
        {
            //Query for the reviews from the database and if entry is present, fill the reviewList.
            String reviewProjection[] = {MovieContract.reviewList.COLUMN_ID,
                    MovieContract.reviewList.COLUMN_AUTHOR,
                    MovieContract.reviewList.COLUMN_CONTENT};

            Uri reviewUri = ContentUris.withAppendedId(MovieContract.reviewList.CONTENT_URI, selMovieId);
            Cursor reviewCursor = mContext.getContentResolver().query(reviewUri, reviewProjection, null, null, null);

            ArrayList<Reviews> reviewList = null;
            if (reviewCursor != null && reviewCursor.getCount() != 0) {
                reviewList = new ArrayList<>();
                try {
                    while (reviewCursor.moveToNext()) {
                        String mId = reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.reviewList.COLUMN_ID));
                        String author = reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.reviewList.COLUMN_AUTHOR));
                        String content = reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.reviewList.COLUMN_CONTENT));
                        Reviews reviews = new Reviews(mId, author, content);
                        reviewList.add(reviews);
                    }
                }finally {
                    reviewCursor.close();
                }
            }

            //Query for the trailers from the database and if entry is present, fill the trailerList.
            ArrayList<Trailer> trailerList = null;
            String trailerProjection[] = {MovieContract.trailerList.COLUMN_ID,
                    MovieContract.trailerList.COLUMN_YOUTUBE_URL,
                    MovieContract.trailerList.COLUMN_THUMBNAIL_URL};

            Uri trailerUri = ContentUris.withAppendedId(MovieContract.trailerList.CONTENT_URI, selMovieId);
            Cursor trailerCursor = mContext.getContentResolver().query(trailerUri, trailerProjection, null, null, null);

            if (trailerCursor != null && trailerCursor.getCount() != 0) {
                trailerList = new ArrayList<>();
                try {
                    while (trailerCursor.moveToNext()) {
                        String mId = trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.trailerList.COLUMN_ID));
                        String trailerUrl = trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.trailerList.COLUMN_YOUTUBE_URL));
                        String trailerThumbNailUrl = trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.trailerList.COLUMN_THUMBNAIL_URL));

                        Trailer trailer = new Trailer(mId, trailerUrl, trailerThumbNailUrl);
                        trailerList.add(trailer);
                    }
                }
                finally {
                    trailerCursor.close();
                }
            }

            MovieData movieData = new MovieData(trailerList, reviewList);
            return movieData;
        } else {

            MovieData movieData = NetworkUtils.fetchReviewsTrailersFromNetwork(movieId);
            if (movieData.getmReviewListList() != null) {
                ContentValues[] trailerValues = JsonToMovieDataUtils.convertJsonTrailerListToContentValue(movieData.getmTrailerList(), Long.parseLong(movieId));
                if (trailerValues != null) {
                    mContext.getContentResolver().bulkInsert(MovieContract.trailerList.CONTENT_URI, trailerValues);
                }
            }

            if (movieData.getmReviewListList() != null) {
                ContentValues[] reviewValues = JsonToMovieDataUtils.convertJsonReviewListToContentValue(movieData.getmReviewListList(), Long.parseLong(movieId));
                if (reviewValues != null) {
                    mContext.getContentResolver().bulkInsert(MovieContract.reviewList.CONTENT_URI, reviewValues);
                }
            }
            return movieData;
        }
    }

    @Override
    protected void onPostExecute(MovieData movieData) {
        super.onPostExecute(movieData);
        mPostAsyncListener.onPostExecuteListener(mContext, movieData);
    }
}
