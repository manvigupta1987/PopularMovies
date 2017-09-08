package com.example.manvi.movieappstage1;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.manvi.movieappstage1.Utils.ConstantsUtils;


public class MovieDetailsActivity extends AppCompatActivity {

    private final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        if(intent!=null)
        {
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(ConstantsUtils.MOVIE_DETAIL, intent.getExtras());
//            if(intent.hasExtra(ConstantsUtils.TABLET_MODE))
//            {
//                bundle.putBoolean(ConstantsUtils.TABLET_MODE, intent.getBooleanExtra(ConstantsUtils.TABLET_MODE,false));
//            }

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(intent.getExtras());

            getSupportFragmentManager().beginTransaction().replace(R.id.container ,movieDetailFragment)
                    .commit();
        }
    }
}
