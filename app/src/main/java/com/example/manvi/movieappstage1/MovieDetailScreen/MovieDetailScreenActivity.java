package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.manvi.movieappstage1.*;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;
import com.example.manvi.movieappstage1.data.Source.local.MoviesLocalDataSource;
import com.example.manvi.movieappstage1.data.Source.remote.MoviesRemoteDataSource;

/**
 * Created by manvi on 13/9/17.
 */

public class MovieDetailScreenActivity extends AppCompatActivity {

    private boolean mTablet;
    private MovieData movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = bundle.getBoolean(ConstantsUtils.TABLET_MODE);
            }
            if (bundle.containsKey(ConstantsUtils.MOVIE_DETAIL)) {
                movieData = bundle.getParcelable(ConstantsUtils.MOVIE_DETAIL);
            }
        }


        MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);

        if (movieDetailFragment == null) {
            movieDetailFragment = new MovieDetailFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, movieDetailFragment);
            transaction.commit();
        }

        MovieRepository movieRepository = MovieRepository.getInstance(MoviesRemoteDataSource.getInstance(),
                MoviesLocalDataSource.getInstance(getApplicationContext()));

        // Create the presenter
        new MovieDetailPresenter(movieData,
                movieRepository,
                movieDetailFragment);
    }
}
