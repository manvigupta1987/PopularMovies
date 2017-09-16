package com.example.manvi.movieappstage1.Utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by manvi on 15/9/17.
 */

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
