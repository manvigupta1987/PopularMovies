package com.example.manvi.movieappstage1.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TimeUtils;

import com.example.manvi.movieappstage1.data.MovieContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Created by manvi on 15/3/17.
 */

public class MovieDataSyncUtils {

    private static boolean sInitialzed;
    private static AsyncTask mBackgroundTask;
    private final static String TAG = MovieDataSyncUtils.class.getSimpleName();

    /*
     * Interval at which to sync with the movie data. Use TimeUnit for convenience, rather than
     * writing out a bunch of multiplication ourselves and risk making a silly mistake.
     */

    private static final int SYNC_INTERVAL_HOURS = 10;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXIBLE = SYNC_INTERVAL_SECONDS /SYNC_INTERVAL_HOURS ;

    private static final String MOVIE_TAG = "sync-movie-data";

    /**
     * Schedules a repeating sync of Movie's data using FirebaseJobDispatcher.
     * @param context Context used to create the GooglePlayDriver that powers the
     *                FirebaseJobDispatcher
     */
    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically sync MovieData App */
        Job syncMovieDataJob = dispatcher.newJobBuilder()
                /* The Service that will be used to sync Movie's data */
                .setService(MovieFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(MOVIE_TAG)
                /*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXIBLE))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(syncMovieDataJob);
    }

    synchronized public static void initailize(final Context context)
    {
        if(sInitialzed)
            return;
        sInitialzed = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = {MovieContract.MovieDataEntry.COLUMN_MOVIE_ID,
                        MovieContract.MovieDataEntry.COLUMN_POSTER_PATH};

                //Query the cursor and check if data is present. If data is not present, start the sync immediately.
                Cursor cursor = context.getContentResolver().query(MovieContract.MovieDataEntry.CONTENT_URI,projection,null,null,null);

                if(cursor == null || cursor.getCount() == 0)
                {
                    startImmediateSync(context);
                }
                else {
                    cursor.close();
                }
            }
        });
        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }

    public final static void startImmediateSync(Context context)
    {
        Intent intent = new Intent(context,MovieDataSyncIntentService.class);
        context.startService(intent);
    }
}
