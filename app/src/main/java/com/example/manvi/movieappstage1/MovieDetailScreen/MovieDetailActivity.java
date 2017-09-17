package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.manvi.movieappstage1.*;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.schedulers.SchedulerProvider;
import com.example.manvi.movieappstage1.data.Movie;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;
import com.example.manvi.movieappstage1.data.Source.local.MoviesLocalDataSource;
import com.example.manvi.movieappstage1.data.Source.remote.MovieApi;
import com.example.manvi.movieappstage1.data.Source.remote.MovieService;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ConstantsUtils.MOVIE_DETAIL)) {
                movie = bundle.getParcelable(ConstantsUtils.MOVIE_DETAIL);
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
        MovieService movieService = MovieApi.getClient().create(MovieService.class);
        MovieRepository movieRepository = MovieRepository.getInstance(MoviesLocalDataSource.getInstance(getApplicationContext(), SchedulerProvider.getInstance()), movieService);

        // Create the presenter
         new MovieDetailPresenter(movie,
                movieRepository,
                movieDetailFragment,
                SchedulerProvider.getInstance());
    }
}
