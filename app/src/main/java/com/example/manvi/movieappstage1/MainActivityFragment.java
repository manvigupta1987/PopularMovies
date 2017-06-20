package com.example.manvi.movieappstage1;

import android.app.ActivityOptions;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manvi.movieappstage1.Adapter.MovieAdapter;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.data.MovieContract;
import com.example.manvi.movieappstage1.sync.MovieDataSyncUtils;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityFragment extends Fragment implements MovieAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivityFragment.class.getSimpleName();

    String[] projection = {MovieContract.MovieDataEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieDataEntry.COLUMN_POSTER_PATH};

    @BindView(R.id.recycler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.pb_loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.error_text_view)
    TextView mNoFavMovie;


    private MovieAdapter mMovieAdapter;
    private int mTabPosition;
    private Boolean mTablet;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        //Check if any data is sent from other acivity.
        Bundle args = getArguments();
        if(args!=null)
        {
            if(args.containsKey(ConstantsUtils.ARG_MOVIE_LIST)) {
                mTabPosition = args.getInt(ConstantsUtils.ARG_MOVIE_LIST);
            }
            if(args.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = args.getBoolean(ConstantsUtils.TABLET_MODE);
            }
        }
        //Bind the data using Butterknife.
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!=null &&
            savedInstanceState.containsKey(ConstantsUtils.TABLET_MODE))
        {
           mTablet = savedInstanceState.getBoolean(ConstantsUtils.TABLET_MODE);
        }

        //calculates the number of columns based on the screen size.
        int columns = calculateColumnsBasedOnScreenSize();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),columns);
        mRecylerView.setLayoutManager(gridLayoutManager);
        mRecylerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(getActivity(),this);

        //check if the scroll position of a layout manager is saved, reterive it.
        //Referred from http://panavtec.me/retain-restore-recycler-view-scroll-position
        if(savedInstanceState!=null &&
                savedInstanceState.containsKey(ConstantsUtils.SAVED_LAYOUT_MANAGER))
        {
           mRecylerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER));
        }

        mRecylerView.setAdapter(mMovieAdapter);

        showLoading();

        //Sync movie data from network if the no data is found in the cursor Or sync the data every 10 hours.
        MovieDataSyncUtils.initailize(getActivity());

        getLoaderManager().initLoader(ConstantsUtils.MOVIE_LOADER_ID,null,this);

        if(savedInstanceState == null) {
            //In case of tablet, set a default screen for the detail pane.
            if (mTablet) {
                defaultFragment();
            }
        }
    }

    /*
        This function calculates the number of columns to be displyed in the grid.
        return Param: Number of Columns.
     */
    private int calculateColumnsBasedOnScreenSize()
    {
        //Calculate device screen size and decides for the number of columns that can be displayed in the grid.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns;
        if(!mTablet) {
            columns = Math.round(dpWidth / 200);
        }else {
            columns = Math.round(dpWidth / 500);
        }

        return columns;
    }


    private void showJsonDataView()
    {
        mRecylerView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);

    }

    private void showLoading()
    {
        mRecylerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    private void showJsonErrorTextView(){
        mRecylerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        mNoFavMovie.setVisibility(View.VISIBLE);
    }

    /*
        when user clicks on the movie, either opens the detail activity with the movie details or shows the detail
        movie fragment in the right pane of tablet.
        Return : void
     */

    @Override
    public void onItemClicked(long movieId) {

        Uri uri = MovieContract.MovieDataEntry.buildMovieDataUriWithMovieID(movieId);
        if(!mTablet) {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.setData(uri);
            intent.putExtra(ConstantsUtils.TABLET_MODE,mTablet);
            startActivity(intent);
        }
        else
        {
            replaceFragment(uri);
        }
    }

    /*
        In case of tablet mode, this functions show the detail fragment on the right side of pane.
     */
    private void replaceFragment(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantsUtils.MOVIE_DETAIL, uri);
        args.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).commit();
    }


    private void defaultFragment(){
        if(mTablet) {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle args = new Bundle();
            args.putString(ConstantsUtils.DEFAULT_TEXT, getString(R.string.default_text));
            args.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);
            movieDetailFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).commit();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        String selection = null;
        String sortBy = null;
        String[] selectionArgs = new String[]{"1"};
        Uri contentUri = MovieContract.MovieDataEntry.CONTENT_URI;
        switch (loaderId) {
            case ConstantsUtils.MOVIE_LOADER_ID: {
                switch (mTabPosition) {
                    case ConstantsUtils.POPULAR_TAB:
                        selection = MovieContract.MovieDataEntry.getSqlSelectPopularMovieCateogry();
                        sortBy = MovieContract.MovieDataEntry.COLUMN_POPULARITY + " DESC";
                        break;
                    case ConstantsUtils.TOP_RATED_TAB:
                        selection = MovieContract.MovieDataEntry.getSqlSelectTopRatedMovieCateogry();
                        sortBy = MovieContract.MovieDataEntry.COLUMN_VOTE_AVG + " DESC";
                        break;
                    case ConstantsUtils.FAVOURITE_TAB:
                        contentUri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
                        selectionArgs = null;
                        projection = null;
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal position");
                }
                return new CursorLoader(getActivity(), contentUri, projection, selection, selectionArgs, sortBy);
            }
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);

        if (data != null && data.getCount() != 0)
        {
           showJsonDataView();
        }
        else
        {
            if(mTabPosition == ConstantsUtils.FAVOURITE_TAB)
            {
                showJsonErrorTextView();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //to store the scroll position of layout manager.
        outState.putParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER, mRecylerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(ConstantsUtils.TABLET_MODE,mTablet);
        super.onSaveInstanceState(outState);
    }
}
