package com.example.manvi.movieappstage1.MoviesScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.manvi.movieappstage1.data.Movie;
import com.example.manvi.movieappstage1.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> mDatasetList;

    private final Context mContext;
    private final ListItemClickListener mlistItemClickListener;

    public MovieAdapter(Context context, ListItemClickListener listItemClickListener, ArrayList<Movie> dataList) {
        mContext = context;
        mlistItemClickListener = listItemClickListener;
        mDatasetList = dataList;
    }

    public interface ListItemClickListener {
        void onItemClicked(Movie movie, View view);
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, final int position) {
        if (mDatasetList != null) {
            Movie movie = mDatasetList.get(position);
            holder.mTVTitle.setText(movie.getTitle());
            holder.mTVTitle.setContentDescription(movie.getTitle());
            holder.mTVRating.setText(String.valueOf(movie.getVoteAvgCount()));
            holder.mTVRating.setContentDescription(holder.mTVRating.getText());
            String poster = movie.getPoster_path();
            if (poster != null) {
                //RGB_565 is used for the memory optimization. R plane spends 5 bit per pixel instead of 8 bits. Same applies to other plane.
//                Picasso.with(mContext).load(poster).placeholder(R.drawable.backdrop_loading_placeholder)
//                        .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(holder.mMovieImage);
                Glide.with(mContext).load(poster)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.backdrop_loading_placeholder)
                        .into(new BitmapImageViewTarget(holder.mMovieImage) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);
                                scheduleStartPostponedTransition(holder.mMovieImage);
                                Palette.from(bitmap).generate(palette -> holder.mTitleBackground.setBackgroundColor(palette.getDarkVibrantColor(ContextCompat.
                                        getColor(mContext, R.color.black_translucent_60))));
                            }
                        });
            }

            holder.mMovieImage.setContentDescription(movie.getTitle());
        }
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((AppCompatActivity)mContext).startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }


    @Override
    public int getItemCount() {
        if (mDatasetList != null) {
            return mDatasetList.size();
        } else {
            return 0;
        }
    }


    public ArrayList<Movie> getDataSetList() {
        return mDatasetList;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster)
        ImageView mMovieImage;
        @BindView(R.id.tv_rating)
        TextView mTVRating;
        @BindView(R.id.tv_title)
        TextView mTVTitle;
        @BindView(R.id.title_background)
        LinearLayout mTitleBackground;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                Movie movie = mDatasetList.get(itemPosition);
                mlistItemClickListener.onItemClicked(movie, view);
            }
        }
    }
}
