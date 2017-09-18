package com.entertainment.manvi.MyMovies.MoviesScreen;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.entertainment.manvi.MyMovies.R;
import com.entertainment.manvi.MyMovies.Utils.schedulers.SchedulerProvider;
import com.entertainment.manvi.MyMovies.data.Source.MovieRepository;
import com.entertainment.manvi.MyMovies.data.Source.local.MoviesLocalDataSource;
import com.entertainment.manvi.MyMovies.data.Source.remote.MovieApi;
import com.entertainment.manvi.MyMovies.data.Source.remote.MovieService;

public class MovieScreenActivity extends AppCompatActivity {

    private MoviePresenter mMoviePresenter;
    private static final String FILTER_TYPE = "filter_type";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_screen);
        MovieFragment tasksFragment =
                (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment

            tasksFragment = MovieFragment.newInstance(0, false);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.contentFrame, tasksFragment);
            transaction.commit();
        }
        MovieService movieService = MovieApi.getClient().create(MovieService.class);
        MovieRepository movieRepository = MovieRepository.getInstance(
                MoviesLocalDataSource.getInstance(getApplicationContext(), SchedulerProvider.getInstance()), movieService);

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
}
