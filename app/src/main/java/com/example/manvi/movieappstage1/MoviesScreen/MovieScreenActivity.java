package com.example.manvi.movieappstage1.MoviesScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.Source.MovieDataSource;
import com.example.manvi.movieappstage1.data.Source.MovieRepository;
import com.example.manvi.movieappstage1.data.Source.local.MoviesLocalDataSource;
import com.example.manvi.movieappstage1.data.Source.remote.MoviesRemoteDataSource;

/**
 * Created by manvi on 12/9/17.
 */

public class MovieScreenActivity extends AppCompatActivity {

    private MoviesPresenter mMoviesPresenter;
    public static final String FILTER_TYPE = "filter_type";

    private boolean mTwoPane; //to check if Tablet layout is required.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if(findViewById(R.id.container)!=null){
            mTwoPane = true;
        }
        else
        {
            mTwoPane = false;
        }

        MovieFragment tasksFragment =
                (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = MovieFragment.newInstance(0, isTablet());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, tasksFragment);
            transaction.commit();
        }

        MovieRepository movieRepository = MovieRepository.getInstance(MoviesRemoteDataSource.getInstance(),
                MoviesLocalDataSource.getInstance(getApplicationContext()));

        mMoviesPresenter = new MoviesPresenter(movieRepository, tasksFragment );
        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            String currentFiltering = savedInstanceState.getString(FILTER_TYPE);
            mMoviesPresenter.setFiltering(currentFiltering);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FILTER_TYPE, mMoviesPresenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    private boolean isTablet()
    {
        return mTwoPane;
    }
}
