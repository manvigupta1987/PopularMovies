package com.example.manvi.movieappstage1.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.manvi.movieappstage1.Utils.NetworkUtils;

/**
 * Created by manvi on 16/3/17.
 */

public class MovieDataSyncIntentService extends IntentService {
    private final static String TAG = MovieDataSyncIntentService.class.getSimpleName();

    public MovieDataSyncIntentService()
    {
        super("MovieDataSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       if(NetworkUtils.isNetworkConnectionAvailable(this)) {
            MovieDataSyncTask.syncMovieData(this);
        }
        else
        {
            Log.e(TAG,"No Internet Connetivity");
        }
    }
}
