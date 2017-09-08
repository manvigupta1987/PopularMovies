package com.example.manvi.movieappstage1;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.manvi.movieappstage1.Adapter.ReviewAdapter;
import com.example.manvi.movieappstage1.Adapter.TrailerAdapter;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.manvi.movieappstage1.Model.MovieData;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements
        FloatingActionButton.OnCheckedChangeListener, FetchTrailerReviewFromNetwork.PostAsyncListener,
        TrailerAdapter.ListItemClickListener{

    private final static String TAG = MovieDetailFragment.class.getSimpleName();
    @BindView(R.id.movie_plot)
    TextView mOverview;
    @BindView(R.id.movie_releaseDate)
    TextView mReleaseDate;
    @BindView(R.id.movie_rating)
    TextView mRating;
    @BindView(R.id.detail_backdrop)
    ImageView mBackDropImage;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.movie_votes)
    TextView mMovieVotes;
    @BindView(R.id.language)
    TextView mMovieLanguage;

    @BindDrawable(R.drawable.no_image)
    Drawable error_image;

    @BindDrawable(R.drawable.backdrop_loading_placeholder)
    Drawable loading_backdrop;

    @BindView(R.id.movie_genre)
    TextView mMovieGenre;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.trailerRecyclerView)
    RecyclerView mTrailerRecyclerView;

    @BindView(R.id.noTrailer_text_view)
    TextView mNoTrailerTextView;

    @BindView(R.id.ReviewRecyclerView)
    RecyclerView mReviewRecyclerView;

    @BindView(R.id.noReview_text_view)
    TextView mNoReviewTextView;

    private FloatingActionButton mFloatingButton;
    private AppBarLayout mAppBarLayoyt;
    private LinearLayout mLinearLayout;
    private TextView mNoMovieDetail;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private Uri mUri;
    private Unbinder unbinder;
    private boolean mTablet;
    private Snackbar snackbar;

    private String shareMovieTitle; //Stores movieTitle which will be used while sending the share intent
    private String shareYouTubeUrlLink; //Stores first link of trailer which will be used while sending the share intent

    private Context mContext;
    private MovieData mMovie;

//    private String[] projection = {MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_BACKDROP,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_POSTER_PATH,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_TITLE,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_RELEASE_DATE,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_VOTE_COUNT,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_VOTE_AVG,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_LANG,
//            MovieContract.MovieDataEntry.TABLE_NAME + "." + MovieContract.MovieDataEntry.COLUMN_MOVIE_ID,
//            " group_concat(" + MovieContract.GenreList.TABLE_NAME + "."
//                    + MovieContract.GenreList.COLUMN_GENRE_NAME + ", ', ' ) as name"};

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        if(args!=null) {
            if (args.containsKey(ConstantsUtils.TABLET_MODE)) {
                mTablet = args.getBoolean(ConstantsUtils.TABLET_MODE);
            }
            if(args.containsKey(ConstantsUtils.MOVIE_DETAIL)) {
                mMovie = args.getParcelable(ConstantsUtils.MOVIE_DETAIL);
                mUri =   MovieContract.FavoriteMovieEntry.buildMovieDataUriWithMovieID(mMovie.getMovieID());
                Log.d(TAG, "Received movie from getArguments() :  " + mMovie.toString());
            }
        }
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, "onActivityCreated");

         if(mTablet) {
            mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.movieDetail);
            mAppBarLayoyt = (AppBarLayout) getActivity().findViewById(R.id.appbar);
            mNoMovieDetail = (TextView) getActivity().findViewById(R.id.default_text_view);

            //showDetailView();
        }
        mFloatingButton = (FloatingActionButton)getActivity().findViewById(R.id.favourite_fab);
        mFloatingButton.setOnCheckedChangeListener(this);

        Bundle args = getArguments();
        if (args!=null && args.containsKey(ConstantsUtils.DEFAULT_TEXT)) {
            if (mTablet) {
                showDefaultTextView();
            }
        }
                //In case of tablet, dont show back button on the action bar for the detail activity.
                if (!mTablet) {
                    setupActionBar();
                }
                //Set the recyclerView with LinearLayout Manager for the trailer View.
                setupTrailerRecyclerView();
                //Set the recyclerView with LinearLayout Manager for the trailer View.
                setupReviewLayout();

                //sync review and trailers form the network.
                startSyncForReviewNTrailersFromNetwork();

                //Check if selected movie is present in the favourites list, if yes, show the floating action button as selected,
                //else unselected.
                checkForFavouriteStatus();
                //start the loader for fetching the movie details from the data base.
                //getLoaderManager().initLoader(ConstantsUtils.MOVIE_DETAIL_LOADER_ID, null, this);
                setupMovieDetails();

        }

    /* Setup the detail activity action Bar in case of phone layout */
    private void setupActionBar()
    {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
        this function will setup the recycler view with horizontal linear layout.
     */
    private void setupTrailerRecyclerView()
    {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(linearLayoutManager1);
        mTrailerRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(getActivity(), this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTrailerRecyclerView.getContext(),
                linearLayoutManager1.getOrientation());
        mTrailerRecyclerView.addItemDecoration(dividerItemDecoration);
        showTrailerDataView();
    }


     /*
        this function will setup the recycler view with vertical linear layout for Reviews.
     */
    private void setupReviewLayout()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(mContext);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        DividerItemDecoration dividerReviewItemDecoration = new DividerItemDecoration(mReviewRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mReviewRecyclerView.addItemDecoration(dividerReviewItemDecoration);
        showReviewDataView();
    }

    /*
       This function fetches the reviews and trailers from the newtork and start the loader
     */
    private void startSyncForReviewNTrailersFromNetwork()
    {
        new FetchTrailerReviewFromNetwork(getActivity(), this).execute(mUri);
    }


    /*
        In case of tablet layout, this function shows the default text view with default message on the detail pane.
     */
    public void showDefaultTextView(){
        if(mTablet) {
            mNoMovieDetail.setVisibility(View.VISIBLE);
            mNoMovieDetail.setText(getArguments().getString(ConstantsUtils.DEFAULT_TEXT));
            mFloatingButton.setVisibility(View.INVISIBLE);
            mLinearLayout.setVisibility(View.INVISIBLE);
            mAppBarLayoyt.setVisibility(View.INVISIBLE);
        }
    }

    /*
        In case of tablet layout, this function hides the default text view on the detail pane and
        shows the details of the movie.
     */
    public void showDetailView(){
        mNoMovieDetail.setVisibility(View.INVISIBLE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mAppBarLayoyt.setVisibility(View.VISIBLE);
        mFloatingButton.setVisibility(View.VISIBLE);
    }

    /*
          This function checks for the selected movie in the data base and based on the cursor result, it will update the state of the
          floating Action button.
     */
    public void checkForFavouriteStatus() {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String[] projection = {MovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID};
                String movieid = mUri.getLastPathSegment();
                Uri uri = ContentUris.withAppendedId(MovieContract.FavoriteMovieEntry.CONTENT_URI, Long.parseLong(movieid));

                //Query the cursor and check if data is present. If data is not present, start the sync immediately.
                return(mContext.getContentResolver().query(uri, projection, null, null, null));
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                if (cursor != null && cursor.getCount() != 0) {
                    mFloatingButton.setCheckedState(true);
                    cursor.close();
                }
            }
        }.execute();
    }

    private void showReviewDataView()
    {
        mReviewRecyclerView.setVisibility(View.VISIBLE);
        mNoReviewTextView.setVisibility(View.INVISIBLE);
    }

    private void showNoReviewTextView()
    {
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        mNoReviewTextView.setVisibility(View.VISIBLE);
    }

    private void showTrailerDataView()
    {
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mNoTrailerTextView.setVisibility(View.INVISIBLE);
    }

    private void showNoTrailerTextView()
    {
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        mNoTrailerTextView.setVisibility(View.VISIBLE);
    }

    public void setupMovieDetails() {
        if(mTablet) {
            showDetailView();
        }
            mOverview.setText(mMovie.getOverview());
            String title = mMovie.getTitle();
            mCollapsingToolbar.setTitle(title);
            mCollapsingToolbar.setContentDescription(title);

            shareMovieTitle = title;
            String releaseDate = mMovie.getReleaseDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(releaseDate);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                String date2 = simpleDateFormat1.format(date);
                mReleaseDate.setText(date2);
                mReleaseDate.setContentDescription(date2);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Double rating = mMovie.getVoteAvgCount();
            String rating_text = rating.toString() + "/10";
            mRating.setText(rating_text);
            mRating.setContentDescription(rating_text);

            Integer votes = mMovie.getVoteCount();
            String votes_text = votes.toString();
            mMovieVotes.setText(votes_text);
            mMovieVotes.setContentDescription(votes_text);


            String lang = mMovie.getOriginalLang();
            mMovieLanguage.setText(lang);
            mMovieLanguage.setContentDescription(lang);

            String posterPath = mMovie.getPoster_path(mContext);
            Picasso.with(getActivity()).load(posterPath).placeholder(R.drawable.backdrop_loading_placeholder)
                    .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(mImageView);

            String backDropImagePath = mMovie.getBackDropPath(mContext);
            Picasso.with(getActivity()).load(backDropImagePath).placeholder(loading_backdrop)
                    .error(error_image).config(Bitmap.Config.RGB_565).into(mBackDropImage);


//            String genreIds = mMovie
//            mMovieGenre.setText(genreIds);
//            mMovieLanguage.setContentDescription(genreIds);

        }


    /*
        This function adds the movies into the favourite list.
     */


//    //This function deletes the movie from the favourite list
//    public int deleteFromFavouritesClickListener(int movieId) {
//        Uri uri = ContentUris.withAppendedId(MovieContract.FavoriteMovieEntry.CONTENT_URI, movieId);
//        return (getContext().getContentResolver().delete(uri, null, null));
//    }

    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {

        UpdateFavouriteMovieDBTask favouriteMovieDBTask = new UpdateFavouriteMovieDBTask(getActivity(), mMovie);
        favouriteMovieDBTask.execute();
        if (isChecked) {
            Snackbar.make(fabView, getString(R.string.Add_fav), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
                Snackbar.make(fabView, getString(R.string.delete_movie), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
    }

    @Override
    public void onPostExecuteListener(Context context, MovieData movieData) {

        Log.e(TAG, "onPostExecuteListener");
        if(movieData!=null) {
            //if trailer list is empty show "No Trailers" on the screen.
            if(movieData.getmTrailerList()!=null) {
                mTrailerAdapter.setTrailerListData(movieData.getmTrailerList());
                //extract the first trailer link from the selected movie. This link will be shared.
                shareYouTubeUrlLink = movieData.getmTrailerList().get(0).getmURL();
            }
            else {
                showNoTrailerTextView();
            }
            //if review list is empty show "No Reviews" on the screen.
            if(movieData.getmReviewListList()!=null)
            {
                mReviewAdapter.setReviewListData(movieData.getmReviewListList());
            }
            else{
                showNoReviewTextView();
            }
        }
    }

    @Override
    public void onItemClicked(String url) {
        if(NetworkUtils.isNetworkConnectionAvailable(getActivity())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            }
        }
        else
        {
            snackbar = snackbar.make(getView(), getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.dismiss), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            snackbar.show();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_detail,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_share:
            Intent shareIntent = createShareYouTubeLinkIntent();
            startActivity(shareIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Create a Intent with movie title and youTube trailer link.
    private Intent createShareYouTubeLinkIntent() {

        String sharedText = "Checkout this movie: \n" + shareMovieTitle + " \nwatch trailer: " + shareYouTubeUrlLink;
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(sharedText)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }
}
