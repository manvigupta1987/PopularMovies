package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 13/9/17.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MovieRepository mMoviesRepository;
    private final MovieDetailContract.View mMovieDetailView;
    private MovieData mMovieData;
    private boolean mTablet;
    private String shareMovieTitle;
    private String shareMovieUrl;

    public MovieDetailPresenter(@Nullable MovieData movieData,
                               @NonNull MovieRepository moviesRepository,
                               @NonNull MovieDetailContract.View movieDetailView) {
        mMovieData = movieData;
        mMoviesRepository = checkNotNull(moviesRepository, "moviesRepository cannot be null!");
        mMovieDetailView = checkNotNull(movieDetailView, "movieDetailView cannot be null!");

        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void start(int page) {
        mMovieDetailView.setupReviewLayout();
        mMovieDetailView.setupTrailerRecyclerView();
        loadReviewsNTrailer();
        checkForFavouriteMovie();
        showMovieDetails();

    }

    @Override
    public void loadReviewsNTrailer() {

        mMoviesRepository.getReviewsTrailers(mMovieData.getMovieID(), new MovieDataSource.GetMovieCallback(){
            @Override
            public void onTaskLoaded(MovieData movieData) {
                if(movieData.getmReviewListList()!=null) {
                    mMovieDetailView.showReviews(true);
                    mMovieDetailView.showReviewsData(movieData.getmReviewListList());
                } else {
                    mMovieDetailView.showReviews(false);
                }
                if(movieData.getmTrailerList()!=null) {
                    mMovieDetailView.showsTrailers(true);
                    mMovieDetailView.showTrailersData(movieData.getmTrailerList());
                    shareMovieUrl = movieData.getmTrailerList().get(0).getmURL();

                }else {
                    mMovieDetailView.showReviews(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mMovieDetailView.showReviews(false);
                mMovieDetailView.showsTrailers(false);
            }
        });
    }

    @Override
    public void addMovieToFavourite() {
        mMoviesRepository.insertMovie(mMovieData);
    }

    @Override
    public void removeMovieFromFavourite() {
        mMoviesRepository.deleteMovie(mMovieData);
    }

    @Override
    public void checkForFavouriteMovie() {
        Long movieId = mMovieData.getMovieID();
        mMoviesRepository.getMovie(movieId.toString(), new MovieDataSource.GetMovieCallback() {
            @Override
            public void onTaskLoaded(MovieData movieData) {
                mMovieDetailView.showMovieStatus(true);
            }

            @Override
            public void onDataNotAvailable() {
                mMovieDetailView.showMovieStatus(false);
            }
        });
    }

    @Override
    public void shareMovie() {
        mMovieDetailView.shareMovie(shareMovieTitle, shareMovieUrl);
    }

    @Override
    public void showMovieDetails() {
        String title = mMovieData.getTitle();
        String overView = mMovieData.getOverview();
        String date = mMovieData.getReleaseDate();
        Double voteAvg = mMovieData.getVoteAvgCount();
        Integer voteCount = mMovieData.getVoteCount();
        String lang = mMovieData.getOriginalLang();
        String poster_path = mMovieData.getPoster_path();
        String backDropImagePath = mMovieData.getBackDropPath();

        shareMovieTitle = title;
        mMovieDetailView.showMovieDetails(overView,title,
                date, voteAvg, voteCount, lang, poster_path, backDropImagePath);
    }
}
