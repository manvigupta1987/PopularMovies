package com.example.manvi.movieappstage1.MovieDetailScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.manvi.movieappstage1.Utils.FloatingActionButton;
import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;


public class MovieDetailFragment extends Fragment implements MovieDetailContract.View,
                                    FloatingActionButton.OnCheckedChangeListener,
                                    View.OnClickListener{
    private MovieDetailContract.Presenter mPresenter;

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
    @BindView(R.id.trailers)
    LinearLayout trailers;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;


    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;


    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.noTrailer_text_view)
    TextView mNoTrailerTextView;

    @BindView(R.id.ReviewRecyclerView)
    RecyclerView mReviewRecyclerView;

    @BindView(R.id.noReview_text_view)
    TextView mNoReviewTextView;

    private FloatingActionButton mFloatingButton;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        getActivity().getWindow().setExitTransition(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFloatingButton = getActivity().findViewById(R.id.favourite_fab);
        mFloatingButton.setOnCheckedChangeListener(this);

        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mFloatingButton.getTop() <= 0) {
                    mFloatingButton.setVisibility(View.INVISIBLE);
                } else {
                    mFloatingButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void showMovieStatus(boolean isFavourite) {
        mFloatingButton.setCheckedState(isFavourite);
    }

    @Override
    public void showsTrailers(boolean isTrailerPresent) {
        if(isTrailerPresent) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            mNoTrailerTextView.setVisibility(View.INVISIBLE);
        }else {
            horizontalScrollView.setVisibility(View.INVISIBLE);
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
       This function will setup the recycler view with vertical linear layout for Reviews.
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
        mOverview.setContentDescription(overView);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setContentDescription(title);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(releaseDate);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
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

        Glide.with(getContext()).
                load(poster_path).
                asBitmap().
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                placeholder(R.color.colorPrimary).
                error(R.drawable.temp).
                into(mImageView);
        mImageView.setContentDescription(title);

        Glide.with(getActivity()).load(backDropImagePath)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.color.colorPrimary)
                .error(R.drawable.temp)
                .into(new BitmapImageViewTarget(mBackDropImage)
                {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim)
                    {
                        super.onResourceReady(bitmap, anim);

                        Palette.from(bitmap).generate(palette -> {
                            int primaryDark = ContextCompat.getColor(getContext(),R.color.colorPrimaryDark);
                            int primary = ContextCompat.getColor(getContext(),R.color.colorPrimary);
                            mCollapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
                            mCollapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
                            setStatusBarColor(palette.getDarkMutedColor(primaryDark));
                        });
                    }
                });
        mBackDropImage.setContentDescription(title);

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
        getActivity().getMenuInflater().inflate(R.menu.share_movie_detail,menu);
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        startActivity(Intent.createChooser(shareIntent,getResources().getText(R.string.sendto)));
    }

    @Override
    public void showTrailersData(ArrayList<Trailer> trailers) {
        if(trailers!=null) {
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);
            this.trailers.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RequestManager glide = Glide.with(getActivity());

            for (Trailer trailer: trailers){
                View thumbContainer = inflater.inflate(R.layout.trailer_item, this.trailers, false);
                String poster = trailer.getThumbNailUrl();
                ImageView thumbView = ButterKnife.findById(thumbContainer, R.id.trailer_image);
                thumbView.setTag(R.id.trailer_image,trailer.getVideoUrl());

                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                glide.load(poster)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.temp)
                        .placeholder(R.color.colorPrimary)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void showReviewsData(ArrayList<Reviews> reviews) {
        if(reviews!=null){
            mReviewAdapter.setReviewListData(reviews);
        }
    }

    @Override
    public void onClick(View view) {
        if(NetworkUtils.isNetworkConnectionAvailable(getActivity()))
        {
            String videoUrl = (String) (view).getTag(R.id.trailer_image);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            }
        }
        else
        {
            Snackbar.make(view, getString(R.string.no_internet), Snackbar.LENGTH_SHORT).show();
        }
    }
}