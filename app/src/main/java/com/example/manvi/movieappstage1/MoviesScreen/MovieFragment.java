package com.example.manvi.movieappstage1.MoviesScreen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.manvi.movieappstage1.MovieDetailFragment;
import com.example.manvi.movieappstage1.MovieDetailsActivity;
import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieData;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 11/9/17.
 */

public class MovieFragment extends Fragment implements MovieScreenContract.View, MovieAdapter.ListItemClickListener {

    private MovieScreenContract.Presenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.pb_loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.error_text_view)
    TextView mNoFavMovie;

    @BindString(R.string.most_popular_movies)
    String mostPopularMovies;
    @BindString(R.string.top_rated_movies)
    String topRatedMovies;
    @BindString(R.string.my_favorite_movies)
    String myFavoriteMovies;

    @BindView(R.id.toolbar1)
    Toolbar mtoolbar;

    private int mPage = 1;
    private MovieAdapter mMovieAdapter;
    private  boolean mTablet;
    private String mFilterType;
   // private int mTabPosition;
//    private ArrayList<MovieData> mFavDataList;
    private ArrayList<MovieData> mDatasetList;
    public static final String SAVE_ALL_MOVIES_LIST = "ALL_MOVIES_LIST";
    public static final String SAVE_FAV_MOVIES_LIST = "FAV_MOVIES_LIST";

    public MovieFragment() {
        // Requires empty public constructor
    }

    public static MovieFragment newInstance(int page, boolean isTablet) {
        MovieFragment fragmentFirst = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantsUtils.ARG_MOVIE_LIST, page);
        args.putBoolean(ConstantsUtils.TABLET_MODE,isTablet);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

//    public static MovieFragment newInstance() {
//        MovieFragment fragmentFirst = new MovieFragment();
//        return fragmentFirst;
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        if(args!=null)
        {
//            if(args.containsKey(ConstantsUtils.ARG_MOVIE_LIST)) {
//                mTabPosition = args.getInt(ConstantsUtils.ARG_MOVIE_LIST);
//            }
            if(args.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = args.getBoolean(ConstantsUtils.TABLET_MODE);
            }
        }

        if(savedInstanceState == null) {
//            if (mTabPosition == ConstantsUtils.FAVOURITE_TAB) {
//                mFavDataList = new ArrayList<MovieData>();
//            } else {
//
//            }
            mDatasetList = new ArrayList<MovieData>();
            if (mTablet) {
                defaultFragment();
            }
        } else {
            mDatasetList = savedInstanceState.getParcelableArrayList(SAVE_ALL_MOVIES_LIST);
//            if(mTabPosition == ConstantsUtils.FAVOURITE_TAB){
//                mFavDataList = savedInstanceState.getParcelableArrayList(SAVE_FAV_MOVIES_LIST);
//            } else {
//
//            }

            if(savedInstanceState.containsKey(ConstantsUtils.SAVED_LAYOUT_MANAGER)){
                //check if the scroll position of a layout manager is saved, reterive it.
                //Referred from http://panavtec.me/retain-restore-recycler-view-scroll-position
                mRecylerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER));
            }
            if(savedInstanceState.containsKey(ConstantsUtils.TABLET_MODE)){
                mTablet = savedInstanceState.getBoolean(ConstantsUtils.TABLET_MODE);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mostPopularMovies);

        int columns = calculateColumnsBasedOnScreenSize();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),columns);
        mRecylerView.setLayoutManager(gridLayoutManager);
        mRecylerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(getActivity(),this, mDatasetList);

        mRecylerView.setAdapter(mMovieAdapter);

        mRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = recyclerView.getChildCount();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int pastVisibleItem =
                            ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                        if(!mFilterType.equals(ConstantsUtils.FAVORITE_MOVIE)) {
                            if (NetworkUtils.isNetworkConnectionAvailable(getActivity())) {
                                mPage++;
                                mPresenter.loadMovies(mPage);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mFilterType = mPresenter.getFiltering();
        mPresenter.start(mPage);
    }

    @Override
    public void setPresenter(MovieScreenContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        //Bind the data using Butterknife.
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void showNoFavMovieError() {
        mRecylerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        mNoFavMovie.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_filter, menu);
    }

    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_movies, popup.getMenu());

        if(mDatasetList!=null){
            mDatasetList.clear();
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popular:
                        mFilterType = ConstantsUtils.POPULAR_MOVIE;
                        mPresenter.setFiltering(ConstantsUtils.POPULAR_MOVIE);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mostPopularMovies);
                        break;
                    case R.id.top_rated:
                        mFilterType = ConstantsUtils.TOP_RATED_MOVIE;
                        mPresenter.setFiltering(ConstantsUtils.TOP_RATED_MOVIE);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(topRatedMovies);
                        break;
                    case R.id.favourite:
                        mFilterType = ConstantsUtils.FAVORITE_MOVIE;
                        mPresenter.setFiltering(ConstantsUtils.FAVORITE_MOVIE);
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myFavoriteMovies);
                        break;
                    default:
                        break;
                }
                mPresenter.loadMovies(1);
                return true;
            }
        });
        popup.show();
    }



    @Override
    public void setLoadingIndicator(boolean active) {
        if(active) {
            mRecylerView.setVisibility(View.INVISIBLE);
            mLoadingBar.setVisibility(View.VISIBLE);
        }else {
            mRecylerView.setVisibility(View.VISIBLE);
            mLoadingBar.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void showMovies(ArrayList<MovieData> movieList) {
        if (movieList != null && movieList.size() != 0) {
            for (MovieData movieData : movieList) {
                mDatasetList.add(movieData);
            }
        }
        //mDatasetList.addAll(movieList);
        mMovieAdapter.notifyDataSetChanged();

        mRecylerView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMovieDetailsUI(MovieData movieData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstantsUtils.MOVIE_DETAIL, movieData);

        if(!mTablet) {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtras(bundle);
            intent.putExtra(ConstantsUtils.TABLET_MODE,mTablet);
            startActivity(intent);
        }
        else
        {
            replaceFragment(movieData);
        }
    }

    /*
        In case of tablet mode, this functions show the detail fragment on the right side of pane.
     */
    private void replaceFragment(MovieData movieData) {
        Bundle args = new Bundle();
        args.putParcelable(ConstantsUtils.MOVIE_DETAIL, movieData);
        args.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, movieDetailFragment).commit();
    }


    @Override
    public void onItemClicked(MovieData movie) {
        mPresenter.openMovieDetails(movie);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //to store the scroll position of layout manager.
        outState.putParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER, mRecylerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(ConstantsUtils.TABLET_MODE,mTablet);
        ArrayList<MovieData> dataList = mMovieAdapter.getDataSetList();
        if (dataList != null && !dataList.isEmpty()) {
            outState.putParcelableArrayList(SAVE_ALL_MOVIES_LIST, dataList);
        }
        super.onSaveInstanceState(outState);
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
}
