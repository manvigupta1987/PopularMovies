package com.example.manvi.movieappstage1.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.manvi.movieappstage1.MainActivityFragment;
import com.example.manvi.movieappstage1.R;

/**
 * Created by manvi on 14/3/17.
 */

// TabLayoutAdapter is required to hold all our fragments which is used by viewpager to manage those fragments
// and its corresponding memory.
public class TabLayoutAdapter extends FragmentPagerAdapter {
    //private final List<String> mFragmentTitleList = new ArrayList<>();
    private boolean mTablet;
    private static int NUM_ITEMS = 3;
    private Context mContext;

    private final String TAG = TabLayoutAdapter.class.getSimpleName();

    /**
     * Create a new {@link TabLayoutAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public TabLayoutAdapter(Context context,FragmentManager fm, boolean isTabletModeEnabled) {
        super(fm);
        mTablet = isTabletModeEnabled;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return MainActivityFragment.newInstance(0,mTablet);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return MainActivityFragment.newInstance(1, mTablet);
            case 2: // Fragment # 1 - This will show SecondFragment
                return MainActivityFragment.newInstance(2, mTablet);
            default:
                return null;
        }
    }


    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

//    public void addFragment(String title)
//    {
//        mFragmentTitleList.add(title);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
// Generate title based on item position
        switch (position){
            case 0:
                return mContext.getString(R.string.popular);
            case 1:
                return mContext.getString(R.string.top_rated);
            case 2:
                return mContext.getString(R.string.favourites);
            default:
                return null;
        }
    }
}
