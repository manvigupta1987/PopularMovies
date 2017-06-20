package com.example.manvi.movieappstage1.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;;

/**
 * Created by manvi on 22/3/17.
 */

public class MovieFirebaseJobService extends JobService {

    private AsyncTask<Void,Void,Void> mBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MovieDataSyncTask.syncMovieData(getApplicationContext());
                jobFinished(jobParameters,false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters,false);
            }
        };
        mBackgroundTask.execute();
        return true;   //indicates that a background thread is running.
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(mBackgroundTask!=null){
            mBackgroundTask.cancel(true);
        }
        return true; //indicates that if job is cancel, it needs to restart again.
    }
}
