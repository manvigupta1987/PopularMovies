package com.entertainment.manvi.MyMovies.MoviesScreen;


import android.support.annotation.NonNull;

import com.entertainment.manvi.MyMovies.Utils.ConstantsUtils;
import com.entertainment.manvi.MyMovies.Utils.schedulers.BaseSchedulerProvider;
import com.entertainment.manvi.MyMovies.data.Movie;
import com.entertainment.manvi.MyMovies.data.Source.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class MoviePresenter implements MovieScreenContract.Presenter {

    private final MovieRepository mMoviesRepository;
    private final MovieScreenContract.View mMoviesView;
    private String mCurrentFiltering = ConstantsUtils.POPULAR_MOVIE;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private final CompositeSubscription mSubscriptions;

    public MoviePresenter(@NonNull MovieRepository moviesRepository,
                          @NonNull MovieScreenContract.View moviesView,
                          @NonNull BaseSchedulerProvider schedulerProvider)
    {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "mSchedulerProvider can not be null");
        mSubscriptions = new CompositeSubscription();
        mMoviesView.setPresenter(this);
    }

    @Override
    public void loadMovies(int page) {
        if(page == 1) {
            mMoviesView.setLoadingIndicator(true);
        }else {
            mMoviesView.showProgress();
        }

        mSubscriptions.clear();

        Observable<List<Movie>> observable = mMoviesRepository.getMovies(mCurrentFiltering, page);
        if (mCurrentFiltering.equals(ConstantsUtils.FAVORITE_MOVIE)) {
            Subscription subscription = observable
                    .subscribeOn(mSchedulerProvider.io())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(
                            //onNext
                            this::processMovies,
                            throwable -> mMoviesView.showNoMovieError());
            mSubscriptions.add(subscription);
        } else {
            Subscription subscription1 = observable.observeOn(mSchedulerProvider.ui())
                    .subscribe(this::processMovies);
            mSubscriptions.add(subscription1);
        }
    }

    private void processMovies(List<Movie> movieList){
        mMoviesView.setLoadingIndicator(false);
        if(movieList!=null && !movieList.isEmpty()) {
            mMoviesView.showMovies((ArrayList<Movie>) movieList);
        }
        else {
            mMoviesView.showNoMovieError();
        }
    }

    @Override
    public void setFiltering(String movieFilter) {
        mCurrentFiltering = movieFilter;
    }


    @Override
    public String getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void subscribe() {
        if(mCurrentFiltering.equals(ConstantsUtils.FAVORITE_MOVIE)  || mMoviesView.isOnline()) {
            loadMovies(1);
        }else {
            mMoviesView.showNoMovieError();
        }
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}