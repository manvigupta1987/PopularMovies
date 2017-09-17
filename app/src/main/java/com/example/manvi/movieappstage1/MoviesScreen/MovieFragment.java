package com.example.manvi.movieappstage1.MoviesScreen;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manvi.movieappstage1.MovieDetailScreen.MovieDetailActivity;
import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.Movie;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class MovieFragment extends Fragment implements MovieScreenContract.View,
        MovieAdapter.ListItemClickListener, View.OnClickListener  {

    private MovieScreenContract.Presenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.pb_loading_bar)
    ProgressBar mLoadingBar;
    @BindView(R.id.error_text_view)
    TextView mNoFavMovie;
    @BindView(R.id.network_retry_full_linearlayout)
    LinearLayout mNoNetworkRetryLayout;
    @BindView(R.id.button_retry)
    Button retryButton;

    @BindString(R.string.most_popular_movies)
    String mostPopularMovies;
    @BindString(R.string.top_rated_movies)
    String topRatedMovies;
    @BindString(R.string.my_favorite_movies)
    String myFavoriteMovies;
    @BindString(R.string.no_internet_message)
    String mNoInternetCon;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    private int mPage = 1;
    private MovieAdapter mMovieAdapter;
    private boolean mTablet;
    private String mFilterType;
    private ActionBar mActionBar;
    private ArrayList<Movie> mDatasetList;
    private static final String SAVE_ALL_MOVIES_LIST = "ALL_MOVIES_LIST";


    public MovieFragment() {
        // Requires empty public constructor
    }

    public static MovieFragment newInstance(int page, boolean isTablet) {
        MovieFragment fragmentFirst = new MovieFragment();
        Bundle args = new Bundle();
        args.putInt(ConstantsUtils.ARG_MOVIE_LIST, page);
        args.putBoolean(ConstantsUtils.TABLET_MODE, isTablet);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = args.getBoolean(ConstantsUtils.TABLET_MODE);
            }
        }

        if (savedInstanceState == null) {
            mDatasetList = new ArrayList<>();
        } else {
            mDatasetList = savedInstanceState.getParcelableArrayList(SAVE_ALL_MOVIES_LIST);
            if (savedInstanceState.containsKey(ConstantsUtils.SAVED_LAYOUT_MANAGER)) {
                //check if the scroll position of a layout manager is saved, reterive it.
                //Referred from http://panavtec.me/retain-restore-recycler-view-scroll-position
                mRecylerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER));
            }
            if (savedInstanceState.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = savedInstanceState.getBoolean(ConstantsUtils.TABLET_MODE);
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);

        int columns = calculateColumnsBasedOnScreenSize();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columns);
        mRecylerView.setLayoutManager(gridLayoutManager);
        mRecylerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(getActivity(), this, mDatasetList);

        mRecylerView.setAdapter(mMovieAdapter);

        retryButton.setOnClickListener(this);
        mRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mFilterType.equals(ConstantsUtils.FAVORITE_MOVIE)) {
                    if (dy > 0) {
                        int visibleItemCount = recyclerView.getChildCount();
                        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        int pastVisibleItem =
                                ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                            if (NetworkUtils.isNetworkConnectionAvailable(getActivity())) {
                                mNoNetworkRetryLayout.setVisibility(View.GONE);
                                mPage++;
                                mPresenter.loadMovies(mPage);
                            }else {
                                if (mDatasetList.isEmpty()) {
                                    mNoNetworkRetryLayout.setVisibility(View.VISIBLE);
                                    Snackbar.make(mRecylerView, mNoInternetCon, Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show();
                                }
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
        setToolBarTitle();
        if(mFilterType.equals(ConstantsUtils.FAVORITE_MOVIE) ||
                mDatasetList!=null && mDatasetList.isEmpty()) {
            mPresenter.subscribe();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        getActivity().getWindow().setEnterTransition(null);
    }

    private void setToolBarTitle() {
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        switch (mFilterType) {
            case ConstantsUtils.POPULAR_MOVIE:
                if(mActionBar!=null) {
                mActionBar.setTitle(mostPopularMovies);
                }
                break;
            case ConstantsUtils.TOP_RATED_MOVIE:
                if(mActionBar!=null) {
                    mActionBar.setTitle(topRatedMovies);
                }
                break;
            case ConstantsUtils.FAVORITE_MOVIE:
                if(mActionBar!=null) {
                    mActionBar.setTitle(myFavoriteMovies);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MovieScreenContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        //Bind the data using Butterknife.
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void showNoMovieError() {
        mRecylerView.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        if(mFilterType.equals(ConstantsUtils.FAVORITE_MOVIE)){
            mNoFavMovie.setVisibility(View.VISIBLE);
            mNoFavMovie.setText(getString(R.string.no_fav));
            mNoNetworkRetryLayout.setVisibility(View.INVISIBLE);
        }else {
            mNoNetworkRetryLayout.setVisibility(View.VISIBLE);
            mNoFavMovie.setVisibility(View.INVISIBLE);
        }
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
        inflater.inflate(R.menu.sort_movie_activity, menu);
    }

    private void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.sort_movie_type, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.popular:
                    mFilterType = ConstantsUtils.POPULAR_MOVIE;
                    mPresenter.setFiltering(ConstantsUtils.POPULAR_MOVIE);
                    mActionBar.setTitle(mostPopularMovies);
                    break;
                case R.id.top_rated:
                    mFilterType = ConstantsUtils.TOP_RATED_MOVIE;
                    mPresenter.setFiltering(ConstantsUtils.TOP_RATED_MOVIE);
                    mActionBar.setTitle(topRatedMovies);
                    break;
                case R.id.favourite:
                    mFilterType = ConstantsUtils.FAVORITE_MOVIE;
                    mPresenter.setFiltering(ConstantsUtils.FAVORITE_MOVIE);
                    mActionBar.setTitle(myFavoriteMovies);
                    break;
                default:
                    break;
            }
            if (mDatasetList != null) {
                mDatasetList.clear();
                mMovieAdapter.notifyDataSetChanged();
            }
            mPresenter.loadMovies(1);
            return true;
        });
        popup.show();
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mRecylerView.setVisibility(View.INVISIBLE);
            mLoadingBar.setVisibility(View.VISIBLE);
        } else {
            mRecylerView.setVisibility(View.VISIBLE);
            mLoadingBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void showMovies(ArrayList<Movie> movieList) {
        if (mFilterType.equals(ConstantsUtils.FAVORITE_MOVIE)) {
            mDatasetList.clear();
        }
        if (movieList != null && movieList.size() != 0) {
            mDatasetList.addAll(movieList);
        }

        mMovieAdapter.notifyDataSetChanged();
        mRecylerView.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        mNoFavMovie.setVisibility(View.INVISIBLE);
        mNoNetworkRetryLayout.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClicked(Movie movie, View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstantsUtils.MOVIE_DETAIL, movie);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(ConstantsUtils.TABLET_MODE, mTablet);
        String transitionName = getString(R.string.transition_string);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),view,transitionName);
        ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
    }


    /*
        This function calculates the number of columns to be displyed in the grid.
        return Param: Number of Columns.
     */
    private int calculateColumnsBasedOnScreenSize() {
        //Calculate device screen size and decides for the number of columns that can be displayed in the grid.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns;
        if (!mTablet) {
            columns = Math.round(dpWidth / 200);
        } else {
            columns = Math.round(dpWidth / 500);
        }

        return columns;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //to store the scroll position of layout manager.
        outState.putParcelable(ConstantsUtils.SAVED_LAYOUT_MANAGER, mRecylerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(ConstantsUtils.TABLET_MODE, mTablet);
        ArrayList<Movie> dataList = mMovieAdapter.getDataSetList();
        if (dataList != null && !dataList.isEmpty()) {
            outState.putParcelableArrayList(SAVE_ALL_MOVIES_LIST, dataList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_retry:
                if (isOnline()) {
                    mNoNetworkRetryLayout.setVisibility(View.GONE);
                    mPresenter.loadMovies(1);
                } else {
                    if (mDatasetList.isEmpty()) {
                        Snackbar.make(mRecylerView,mNoInternetCon,Snackbar.LENGTH_SHORT).show();
                        mNoNetworkRetryLayout.setVisibility(View.VISIBLE);
                    }
                }

                break;
        }
    }

    public boolean isOnline(){
        return NetworkUtils.isNetworkConnectionAvailable(getContext());
    }
}
