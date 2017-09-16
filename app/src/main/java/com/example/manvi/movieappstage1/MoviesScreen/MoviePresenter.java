package com.example.manvi.movieappstage1.MoviesScreen;


import android.support.annotation.NonNull;

import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.schedulers.BaseSchedulerProvider;
import com.example.manvi.movieappstage1.data.Movie;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 12/9/17.
 */

public class MoviePresenter implements MovieScreenContract.Presenter {

    private final MovieRepository mMoviesRepository;
    private final MovieScreenContract.View mMoviesView;
    private String mCurrentFiltering = ConstantsUtils.POPULAR_MOVIE;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public MoviePresenter(@NonNull MovieRepository moviesRepository,
                          @NonNull MovieScreenContract.View moviesView,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "mSchedulerProvider can not be null");

        mSubscriptions = new CompositeSubscription();
        mMoviesView.setPresenter(this);
    }


    @Override
    public void loadMovies(int page) {
        mMoviesView.setLoadingIndicator(true);
        mSubscriptions.clear();

        Observable<List<Movie>> observable = mMoviesRepository.getMovies(mCurrentFiltering, page);
        if (mCurrentFiltering == ConstantsUtils.FAVORITE_MOVIE) {
            Subscription subscription = observable
                    .subscribeOn(mSchedulerProvider.io())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(
                            //onNext
                            this::processMovies,
                            throwable -> mMoviesView.showNoFavMovieError());
            mSubscriptions.add(subscription);
        } else {

            Subscription subscription1 = observable.observeOn(mSchedulerProvider.ui())
                    .subscribe(this::processMovies);
            mSubscriptions.add(subscription1);
        }
    }

    public void processMovies(List<Movie> movieList){
        if(movieList!=null && !movieList.isEmpty()) {
            mMoviesView.setLoadingIndicator(false);
            mMoviesView.showMovies((ArrayList<Movie>) movieList);
        }
    }

    @Override
    public void openMovieDetails(Movie movie) {
        checkNotNull(movie, "requestedMovie cannot be null!");
        mMoviesView.showMovieDetailsUI(movie);
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
        loadMovies(1);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
