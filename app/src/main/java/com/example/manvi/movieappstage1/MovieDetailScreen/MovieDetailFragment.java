package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manvi.movieappstage1.Utils.FloatingActionButton;
import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.ConstantsUtils;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by manvi on 13/9/17.
 */

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View,
                                    FloatingActionButton.OnCheckedChangeListener,
                                    TrailerAdapter.ListItemClickListener{
    MovieDetailContract.Presenter mPresenter;

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

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

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
    private Snackbar snackbar;
    private boolean mTablet;
    private Context mContext;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mTablet) {
            mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.movieDetail);
            mAppBarLayoyt = (AppBarLayout) getActivity().findViewById(R.id.appbar);
            mNoMovieDetail = (TextView) getActivity().findViewById(R.id.default_text_view);
        }
        mFloatingButton = (FloatingActionButton)getActivity().findViewById(R.id.favourite_fab);
        mFloatingButton.setOnCheckedChangeListener(this);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void showDefaultTextView(){
        if(mTablet) {
            mNoMovieDetail.setVisibility(View.VISIBLE);
            mNoMovieDetail.setText(getArguments().getString(ConstantsUtils.DEFAULT_TEXT));
            mFloatingButton.setVisibility(View.INVISIBLE);
            mLinearLayout.setVisibility(View.INVISIBLE);
            mAppBarLayoyt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMovieStatus(boolean isFavourite) {
        mFloatingButton.setCheckedState(isFavourite);
    }

    /*
       this function will setup the recycler view with horizontal linear layout.
     */
    @Override
    public void setupTrailerRecyclerView()
    {
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(linearLayoutManager1);
        mTrailerRecyclerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(getActivity(), this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTrailerRecyclerView.getContext(),
                linearLayoutManager1.getOrientation());
        mTrailerRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void showsTrailers(boolean isTrailerPresent) {
        if(isTrailerPresent) {
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
            mNoTrailerTextView.setVisibility(View.INVISIBLE);
        }else {
            mTrailerRecyclerView.setVisibility(View.INVISIBLE);
            mNoTrailerTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showReviews(boolean isReviewPresent) {
        if(isReviewPresent) {
            mReviewRecyclerView.setVisibility(View.VISIBLE);
            mNoReviewTextView.setVisibility(View.INVISIBLE);
        }else {
            mReviewRecyclerView.setVisibility(View.INVISIBLE);
            mNoReviewTextView.setVisibility(View.VISIBLE);
        }
    }

    /*
               this function will setup the recycler view with vertical linear layout for Reviews.
            */
    @Override
    public void setupReviewLayout()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        DividerItemDecoration dividerReviewItemDecoration = new DividerItemDecoration(mReviewRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mReviewRecyclerView.addItemDecoration(dividerReviewItemDecoration);
    }



    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMovieDetails(String overView, String title, String releaseDate, Double voteAvg, Integer voteCount, String lang, String poster_path,
                                 String backDropImagePath) {

        mOverview.setText(overView);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setContentDescription(title);

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

        String rating_text = voteAvg.toString() + "/10";
        mRating.setText(rating_text);
        mRating.setContentDescription(rating_text);

        String votes_text = voteCount.toString();
        mMovieVotes.setText(votes_text);
        mMovieVotes.setContentDescription(votes_text);

        mMovieLanguage.setText(lang);
        mMovieLanguage.setContentDescription(lang);

        Picasso.with(getActivity()).load(poster_path).placeholder(R.drawable.backdrop_loading_placeholder)
                .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(mImageView);

        Picasso.with(getActivity()).load(backDropImagePath).placeholder(loading_backdrop)
                .error(error_image).config(Bitmap.Config.RGB_565).into(mBackDropImage, new Callback() {
            @Override
            public void onSuccess() {
                if(!mTablet){
                    createPalette();
                }
            }

            @Override
            public void onError() {

            }
        });


    }

    //Sets Collapsing toolbar Color ContentScrim and StatusBar Scrim color based on the backdrop color swatches.
    private void createPalette(){
        Bitmap bitmap = ((BitmapDrawable) mBackDropImage.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = ContextCompat.getColor(getContext(),R.color.colorPrimaryDark);
                int primary = ContextCompat.getColor(getContext(),R.color.colorPrimary);
                mCollapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
                mCollapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
                setStatusBarColor(palette.getDarkMutedColor(primaryDark));
            }
        });
    }


    //Setting the color of status bar as per the backDrop image.
    private void setStatusBarColor(int darkMutedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkMutedColor);
        }
    }

    @Override
    public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        if(isChecked) {
            mPresenter.addMovieToFavourite();
            Snackbar.make(fabView, getString(R.string.Add_fav), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {
            mPresenter.removeMovieFromFavourite();
            Snackbar.make(fabView, getString(R.string.delete_movie), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
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
                createShareYouTubeLinkIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Create a Intent with movie title and youTube trailer link.
    private void createShareYouTubeLinkIntent() {
        mPresenter.shareMovie();
    }

    @Override
    public void shareMovie(String title, String urlLink) {
        String sharedText = "Checkout this movie: \n" + title + " \nwatch trailer: " + urlLink;
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(sharedText)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(shareIntent);
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
    public void showTrailersData(ArrayList<Trailer> trailers) {
        if(trailers!=null) {
            mTrailerAdapter.setTrailerListData(trailers);
        }
    }

    @Override
    public void showReviewsData(ArrayList<Reviews> reviews) {
        if(reviews!=null){
            mReviewAdapter.setReviewListData(reviews);
        }
    }
}
