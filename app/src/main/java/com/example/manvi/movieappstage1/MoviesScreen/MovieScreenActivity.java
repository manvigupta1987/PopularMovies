package com.example.manvi.movieappstage1.MoviesScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.schedulers.SchedulerProvider;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;
import com.example.manvi.movieappstage1.data.Source.local.MoviesLocalDataSource;
import com.example.manvi.movieappstage1.data.Source.remote.MovieApi;
import com.example.manvi.movieappstage1.data.Source.remote.MovieService;

/**
 * Created by manvi on 12/9/17.
 */

public class MovieScreenActivity extends AppCompatActivity {

    private MoviePresenter mMoviePresenter;
    private MovieService movieService;
    public static final String FILTER_TYPE = "filter_type";

    private boolean mTwoPane = false; //to check if Tablet layout is required.


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_screen);

        MovieFragment tasksFragment =
                (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = MovieFragment.newInstance(0, isTablet());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, tasksFragment);
            transaction.commit();
        }
        movieService = MovieApi.getClient().create(MovieService.class);
        MovieRepository movieRepository = MovieRepository.getInstance(
                MoviesLocalDataSource.getInstance(getApplicationContext(), SchedulerProvider.getInstance()),movieService);

        mMoviePresenter = new MoviePresenter(movieRepository, tasksFragment, SchedulerProvider.getInstance());
        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            String currentFiltering = savedInstanceState.getString(FILTER_TYPE);
            mMoviePresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FILTER_TYPE, mMoviePresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    private boolean isTablet()
    {
        return mTwoPane;
    }
}
