package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.manvi.movieappstage1.Utils.schedulers.BaseSchedulerProvider;
import com.example.manvi.movieappstage1.data.Movie;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;
import com.example.manvi.movieappstage1.data.Trailer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 13/9/17.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MovieRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private Movie mMovie;
    private String shareMovieTitle;
    private String shareMovieUrl;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscribtion;

    public MovieDetailPresenter(@Nullable Movie movie,
                                @NonNull MovieRepository moviesRepository,
                                @NonNull MovieDetailContract.View movieDetailView,
                                @NonNull BaseSchedulerProvider schedulerProvider) {
        mMovie = movie;
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null!");
        mMovieDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider can not be null");

        mSubscribtion = new CompositeSubscription();
        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void loadReviews() {

        //mSubscribtion.clear();
        Subscription subscription1;
        Observable<List<Reviews>> observable = mMoviesRepository.getReviews(mMovie.getMovieID());
        subscription1 = observable.observeOn(mSchedulerProvider.ui())
                .subscribe(this::processReviews,
                        throwable -> mMovieDetailView.showReviews(false));
        mSubscribtion.add(subscription1);
    }

    public void processReviews(List<Reviews> reviews) {
        if (reviews != null) {
            mMovieDetailView.showReviews(true);
            mMovieDetailView.showReviewsData((ArrayList<Reviews>) reviews);
        } else {
            mMovieDetailView.showReviews(false);
        }
    }

    @Override
    public void loadTrailers() {
        mSubscribtion.clear();
        Subscription subscription;
        Observable<List<Trailer>> observable = mMoviesRepository.getTrailer(mMovie.getMovieID());
        subscription = observable.observeOn(mSchedulerProvider.ui())
                .subscribe(this::processTrailer,
                        throwable -> mMovieDetailView.showsTrailers(false));
        mSubscribtion.add(subscription);
    }

    public void processTrailer(List<Trailer> trailer) {
        if (trailer != null) {
            mMovieDetailView.showsTrailers(true);
            mMovieDetailView.showTrailersData((ArrayList<Trailer>) trailer);
            shareMovieUrl = trailer.get(0).getVideoUrl();
        } else {
            mMovieDetailView.showsTrailers(false);
        }
    }

    @Override
    public void addMovieToFavourite() {
        mMoviesRepository.insertMovie(mMovie);
    }

    @Override
    public void removeMovieFromFavourite() {
        mMoviesRepository.deleteMovie(mMovie);
    }

    @Override
    public void checkForFavouriteMovie() {
        Long movieId = mMovie.getMovieID();
        mSubscribtion.add(mMoviesRepository.getMovie(movieId.toString())
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(
                        //onNext
                        this::showStatus,
                        throwable -> mMovieDetailView.showMovieStatus(false)));
    }

    private void showStatus(Movie movie) {
        mMovieDetailView.showMovieStatus(true);
    }


    @Override
    public void shareMovie() {
        mMovieDetailView.shareMovie(shareMovieTitle, shareMovieUrl);
    }

    @Override
    public void showMovieDetails() {
        String title = mMovie.getTitle();
        String overView = mMovie.getOverview();
        String date = mMovie.getReleaseDate();
        Double voteAvg = mMovie.getVoteAvgCount();
        Integer voteCount = mMovie.getVoteCount();
        String lang = mMovie.getOriginalLang();
        String poster_path = mMovie.getPoster_path();
        String backDropImagePath = mMovie.getBackDropPath();

        shareMovieTitle = title;
        mMovieDetailView.showMovieDetails(overView, title,
                date, voteAvg, voteCount, lang, poster_path, backDropImagePath);
    }

    @Override
    public void subscribe() {
        mMovieDetailView.setupReviewLayout();
        mMovieDetailView.setupTrailerRecyclerView();
        loadTrailers();
        loadReviews();
        checkForFavouriteMovie();
        showMovieDetails();
    }

    @Override
    public void unsubscribe() {
        mSubscribtion.clear();
    }
}
