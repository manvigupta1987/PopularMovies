package com.example.manvi.movieappstage1;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.example.manvi.movieappstage1.MoviesScreen.TabLayoutAdapter;

public class MainActivity extends AppCompatActivity{

//    private final String TAG = MainActivity.class.getSimpleName();
//    private boolean mTwoPane; //to check if Tablet layout is required.
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        if(findViewById(R.id.container)!=null){
//            mTwoPane = true;
//        }
//        else
//        {
//            mTwoPane = false;
//        }
//
//        TabLayoutAdapter adapter = new TabLayoutAdapter(this,getSupportFragmentManager(),isTablet());
//        // Create an adapter that knows which fragment should be shown on each page
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.setPageTransformer(true, new AccordionTransformer());
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//    }
//
//    private boolean isTablet()
//    {
//        return mTwoPane;
//    }
}

