package com.example.manvi.movieappstage1.Adapter;

import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.manvi.movieappstage1.MainActivityFragment;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manvi on 14/3/17.
 */

// TabLayoutAdapter is required to hold all our fragments which is used by viewpager to manage those fragments
// and its corresponding memory.
public class TabLayoutAdapter extends FragmentPagerAdapter {
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private boolean mTablet;

    private final String TAG = TabLayoutAdapter.class.getSimpleName();

    /**
     * Create a new {@link TabLayoutAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public TabLayoutAdapter(FragmentManager fm, boolean isTabletModeEnabled) {
        super(fm);
        mTablet = isTabletModeEnabled;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantsUtils.ARG_MOVIE_LIST, position);
        args.putBoolean(ConstantsUtils.TABLET_MODE,mTablet);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    public void addFragment(String title)
    {
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
// Generate title based on item position
        return mFragmentTitleList.get(position);
    }

}
