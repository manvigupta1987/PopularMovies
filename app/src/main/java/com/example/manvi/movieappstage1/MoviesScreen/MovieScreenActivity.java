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

//        TabLayoutAdapter adapter = new TabLayoutAdapter(this,getSupportFragmentManager(),isTablet());
//        // Create an adapter that knows which fragment should be shown on each page
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.setPageTransformer(true, new AccordionTransformer());
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);

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

        //mMoviesPresenter = new MoviesPresenter(movieRepository, (MovieFragment)adapter.getItem(viewPager.getCurrentItem()));
        mMoviesPresenter = new MoviesPresenter(movieRepository, tasksFragment );
    }

    private boolean isTablet()
    {
        return mTwoPane;
    }
}
