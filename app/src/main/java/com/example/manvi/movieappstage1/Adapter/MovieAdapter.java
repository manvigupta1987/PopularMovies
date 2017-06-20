package com.example.manvi.movieappstage1.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manvi.movieappstage1.R;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manvi on 1/3/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {



    private final String TAG = MovieAdapter.class.getSimpleName();
    private Cursor mCursor;

    private Context mContext;
    private ListItemClickListener mlistItemClickListener;

    public MovieAdapter(Context context, ListItemClickListener listItemClickListener){
        mContext = context;
        mlistItemClickListener = listItemClickListener;
    }

    public interface ListItemClickListener{
        public void onItemClicked(long movieId);
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
        if(!(mCursor.moveToPosition(position)))
        {
            return;
        }

        String poster = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieDataEntry.COLUMN_POSTER_PATH));
        if(poster!=null) {
            //RGB_565 is used for the memory optimization. R plane spends 5 bit per pixel instead of 8 bits. Same applies to other plane.
            Picasso.with(mContext).load(poster).placeholder(R.drawable.backdrop_loading_placeholder)
                    .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(holder.mMovieImage);
        }
    }


    @Override
    public int getItemCount() {
        if(mCursor == null)
        {
            return 0;
        }
        else
        {
            return mCursor.getCount();
        }
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.movie_image) ImageView mMovieImage;
        //@BindView(R.id.vote_average) TextView mVoteAverage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            long movieId = mCursor.getLong(mCursor.getColumnIndex(MovieContract.MovieDataEntry.COLUMN_MOVIE_ID));
            mlistItemClickListener.onItemClicked(movieId);
        }
    }

    public void swapCursor(Cursor cursor)
    {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
