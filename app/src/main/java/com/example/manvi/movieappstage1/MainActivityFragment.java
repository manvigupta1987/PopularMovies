package com.example.manvi.movieappstage1;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manvi.movieappstage1.MoviesScreen.MovieAdapter;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.Source.local.MovieContract;


import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivityFragment {

//    private final String TAG = MainActivityFragment.class.getSimpleName();
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecylerView;
//    @BindView(R.id.pb_loading_bar)
//    ProgressBar mLoadingBar;
//    @BindView(R.id.error_text_view)
//    TextView mNoFavMovie;
//    private int mPage = 1;
//
//
//    private MovieAdapter mMovieAdapter;
//    private  int mTabPosition;
//    private  boolean mTablet;
//    private ArrayList<MovieData> mFavDataList;
//    private ArrayList<MovieData> mDatasetList;
//    public static final String SAVE_ALL_MOVIES_LIST = "ALL_MOVIES_LIST";
//    public static final String SAVE_FAV_MOVIES_LIST = "FAV_MOVIES_LIST";
//
//
//    public MainActivityFragment() {
//        // Required empty public constructorteMov
//    }
//
//    public static MainActivityFragment newInstance(int page, boolean isTablet) {
//        MainActivityFragment fragmentFirst = new MainActivityFragment();
//        Bundle args = new Bundle();
//        args.putInt(ConstantsUtils.ARG_MOVIE_LIST, page);
//        args.putBoolean(ConstantsUtils.TABLET_MODE,isTablet);
//        fragmentFirst.setArguments(args);
//        return fragmentFirst;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //Retain this fragment across configuration changes.
//        setRetainInstance(true);
//        Bundle args = getArguments();
//        if(args!=null)
//        {
//            if(args.containsKey(ConstantsUtils.ARG_MOVIE_LIST)) {
//                mTabPosition = args.getInt(ConstantsUtils.ARG_MOVIE_LIST);
//            }
//            if(args.containsKey(ConstantsUtils.TABLET_MODE)) {
//                mTablet = args.getBoolean(ConstantsUtils.TABLET_MODE);
//            }
//        }
//
//        if(savedInstanceState == null) {
//            if (mTabPosition == ConstantsUtils.FAVOURITE_TAB) {
//                mFavDataList = new ArrayList<MovieData>();
//            } else {
//                mDatasetList = new ArrayList<MovieData>();
//            }
//
//            if (mTablet) {
//                defaultFragment();
//            }
//        } else {
//            if(mTabPosition == ConstantsUtils.FAVOURITE_TAB){
//                mFavDataList = savedInstanceState.getParcelableArrayList(SAVE_FAV_MOVIES_LIST);
//            } else {
//                mDatasetList = savedInstanceState.getParcelableArrayList(SAVE_ALL_MOVIES_LIST);
//            }
//
//            if(savedInstanceState.containsKey(ConstantsUtils.SAVED_LAYOUT_MANAGER)){
//                //check if the scroll position of a layout manager is saved, reterive it.
//                //Referred from http://panavtec.me/retain-restore-recycler-view-scroll-position
//                mRecylerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER));
//            }
//            if(savedInstanceState.containsKey(ConstantsUtils.TABLET_MODE)){
//                mTablet = savedInstanceState.getBoolean(ConstantsUtils.TABLET_MODE);
//            }
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
//        //Bind the data using Butterknife.
//        ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        int columns = calculateColumnsBasedOnScreenSize();
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),columns);
//        mRecylerView.setLayoutManager(gridLayoutManager);
//        mRecylerView.setHasFixedSize(true);
//
//        mMovieAdapter = new MovieAdapter(getActivity(),this,mDatasetList);
//
//        mRecylerView.setAdapter(mMovieAdapter);
//
//        mRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0) {
//                    int visibleItemCount = recyclerView.getChildCount();
//                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                    int pastVisibleItem =
//                            ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                    if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
//                        if(mTabPosition!= ConstantsUtils.FAVOURITE_TAB) {
//                            if (isNetworkConnectionAvailable(getActivity())) {
//                                mPage++;
//                                fetchMovies(false);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(mFavDataList!=null) {
//            mFavDataList.clear();
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if ((mDatasetList!=null && mDatasetList.isEmpty())
//                || (mFavDataList!=null && mFavDataList.isEmpty())) {
//            fetchMovies(true);
//        }
//    }
//
//    private void fetchMovies(boolean refresh){
//        if (refresh) {
//            if(mDatasetList!=null) {
//                mDatasetList.clear();
//                mMovieAdapter.setDatasetList(mDatasetList);
//                mPage = 1;
//            }
//        }
//        showLoading();
//        switch(mTabPosition) {
//            case ConstantsUtils.POPULAR_TAB:
//                new LoadMoviesTask().execute(ConstantsUtils.POPULAR_MOVIE);
//                break;
//            case ConstantsUtils.TOP_RATED_TAB:
//                new LoadMoviesTask().execute(ConstantsUtils.TOP_RATED_MOVIE);
//                break;
//            case ConstantsUtils.FAVOURITE_TAB:
//                new LoadFavoriteMoviesTask().execute();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private class LoadMoviesTask extends AsyncTask<String, Void, ArrayList<MovieData>> {
//        ArrayList<MovieData> movieDataList;
//        @Override
//        protected ArrayList<MovieData> doInBackground(String... params) {
//            // Retrieve movie records from fav movie tablem
//            movieDataList = NetworkUtils.fetchMovieInfoFromNetwork(params[0], mPage);
//            return movieDataList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<MovieData> movieDataList) {
//            super.onPostExecute(movieDataList);
//            if (movieDataList != null && movieDataList.size() != 0) {
//            for(MovieData movieData: movieDataList){
//                mDatasetList.add(movieData);
//            }
//            mMovieAdapter.notifyDataSetChanged();
//            showJsonDataView();
//            }
//        }
//    }
//
//    /*
//        This function calculates the number of columns to be displyed in the grid.
//        return Param: Number of Columns.
//     */
//    private int calculateColumnsBasedOnScreenSize()
//    {
//        //Calculate device screen size and decides for the number of columns that can be displayed in the grid.
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float density  = getResources().getDisplayMetrics().density;
//        float dpWidth  = outMetrics.widthPixels / density;
//        int columns;
//        if(!mTablet) {
//            columns = Math.round(dpWidth / 200);
//        }else {
//            columns = Math.round(dpWidth / 500);
//        }
//
//        return columns;
//    }
//
//
//    private void showJsonDataView()
//    {
//        mRecylerView.setVisibility(View.VISIBLE);
//        mLoadingBar.setVisibility(View.INVISIBLE);
//
//    }
//
//    private void showLoading()
//    {
//        mRecylerView.setVisibility(View.INVISIBLE);
//        mLoadingBar.setVisibility(View.VISIBLE);
//    }
//
//    private void showJsonErrorTextView(){
//        mRecylerView.setVisibility(View.INVISIBLE);
//        mLoadingBar.setVisibility(View.INVISIBLE);
//        mNoFavMovie.setVisibility(View.VISIBLE);
//    }
//
//    /*
//        when user clicks on the movie, either opens the detail activity with the movie details or shows the detail
//        movie fragment in the right pane of tablet.
//        Return : void
//     */
//
//    @Override
//    public void onItemClicked(MovieData movieData) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(ConstantsUtils.MOVIE_DETAIL, movieData);
//
//        if(!mTablet) {
//            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
//            intent.putExtras(bundle);
//            intent.putExtra(ConstantsUtils.TABLET_MODE,mTablet);
//            startActivity(intent);
//        }
//        else
//        {
//            replaceFragment(movieData);
//        }
//    }
//
//    /*
//        In case of tablet mode, this functions show the detail fragment on the right side of pane.
//     */
//    private void replaceFragment(MovieData movieData) {
//        Bundle args = new Bundle();
//        args.putParcelable(ConstantsUtils.MOVIE_DETAIL, movieData);
//        args.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);
//
//        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
//        movieDetailFragment.setArguments(args);
//
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).commit();
//    }
//
//
//    private void defaultFragment(){
//        if(mTablet) {
//            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
//            Bundle args = new Bundle();
//            args.putString(ConstantsUtils.DEFAULT_TEXT, getString(R.string.default_text));
//            args.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);
//            movieDetailFragment.setArguments(args);
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).commit();
//        }
//    }
//
//
//    private class LoadFavoriteMoviesTask extends AsyncTask<Void, Integer, ArrayList<MovieData>> {
//        @Override
//        protected ArrayList<MovieData> doInBackground(Void... params) {
//            // Retrieve movie records from fav movie table
//            //mDatasetList.clear();
//            Uri favoriteMovieUri = MovieContract.FavoriteMovieEntry.CONTENT_URI;
//            Cursor favMovieCursor = getActivity().getContentResolver().query(
//                    favoriteMovieUri,
//                    null,
//                    null,
//                    null,
//                    null);
//
//            if (favMovieCursor.moveToFirst()) {
//                do {
//                    MovieData movie = new MovieData(favMovieCursor.getInt(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_LANG)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)),
//                            favMovieCursor.getString(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)),
//                            favMovieCursor.getDouble(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVG)),
//                            favMovieCursor.getDouble(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POPULARITY)),
//                            favMovieCursor.getInt(favMovieCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT))
//                    );
//                    mFavDataList.add(movie);
//                } while (favMovieCursor.moveToNext());
//            }
//
//            favMovieCursor.close();
//            return mFavDataList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<MovieData> datalist) {
//            super.onPostExecute(datalist);
//            //mMovieAdapter.setDatasetList(datalist);
//            if (datalist!=null && datalist.size() != 0)
//            {
//                showJsonDataView();
//            }
//            else
//            {
//                if(mTabPosition == ConstantsUtils.FAVOURITE_TAB)
//                {
//                    showJsonErrorTextView();
//                }
//            }
//            mMovieAdapter.setDatasetList(mFavDataList);
//            mMovieAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        //to store the scroll position of layout manager.
//        outState.putParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER, mRecylerView.getLayoutManager().onSaveInstanceState());
//        outState.putBoolean(ConstantsUtils.TABLET_MODE,mTablet);
//        ArrayList<MovieData> dataList = mMovieAdapter.getDataSetList();
//        if (dataList != null && !dataList.isEmpty()) {
//            outState.putParcelableArrayList(SAVE_ALL_MOVIES_LIST, dataList);
//        }
//        super.onSaveInstanceState(outState);
//    }
}
