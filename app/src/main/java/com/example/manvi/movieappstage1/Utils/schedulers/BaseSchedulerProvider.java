package com.example.manvi.movieappstage1.Utils.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;


public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
