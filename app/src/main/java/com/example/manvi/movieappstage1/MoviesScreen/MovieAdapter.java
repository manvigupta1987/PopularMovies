package com.example.manvi.movieappstage1.MoviesScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manvi on 1/3/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<MovieData> mDatasetList;

    private Context mContext;
    private ListItemClickListener mlistItemClickListener;

    public MovieAdapter(Context context, ListItemClickListener listItemClickListener, ArrayList<MovieData> dataList){
        mContext = context;
        mlistItemClickListener = listItemClickListener;
        mDatasetList = dataList;
    }

    public interface ListItemClickListener{
        void onItemClicked(MovieData movie);
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup,false);
        MovieAdapterViewHolder movieAdapterViewHolder = new MovieAdapterViewHolder(view);
        return movieAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, final int position)
    {
        if(mDatasetList!=null) {
            MovieData movieData = mDatasetList.get(position);
            holder.mTVTitle.setText(movieData.getTitle());
            holder.mTVRating.setText(String.valueOf(movieData.getVoteAvgCount()));
            String poster = movieData.getPoster_path();
            if (poster != null) {
                //RGB_565 is used for the memory optimization. R plane spends 5 bit per pixel instead of 8 bits. Same applies to other plane.
                Picasso.with(mContext).load(poster).placeholder(R.drawable.backdrop_loading_placeholder)
                        .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(holder.mMovieImage);
            }
        }
    }


    @Override
    public int getItemCount() {
        if(mDatasetList!=null) {
            return mDatasetList.size();
        }else {
            return 0;
        }
    }


    public ArrayList<MovieData> getDataSetList(){
        return mDatasetList;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.movie_image) ImageView mMovieImage;
        @BindView(R.id.tv_rating)
        TextView mTVRating;
        @BindView(R.id.tv_title)
        TextView mTVTitle;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                MovieData movie = mDatasetList.get(itemPosition);
                mlistItemClickListener.onItemClicked(movie);
            }
        }
    }
}
