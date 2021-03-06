package com.example.manvi.movieappstage1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.manvi.movieappstage1.Model.Trailer;
import com.example.manvi.movieappstage1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manvi on 20/3/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.trailerAdapterViewHolder> {

    private final String TAG = TrailerAdapter.class.getSimpleName();
    private ArrayList<Trailer> mTrailerList;

    private Context mContext;
    public ListItemClickListener mlistItemClickListener;

    public TrailerAdapter(Context context, ListItemClickListener listItemClickListener){
        mContext = context;
        mlistItemClickListener = listItemClickListener;
    }

    public interface ListItemClickListener{
        public void onItemClicked(String url);
    }


    @Override
    public trailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup,false);
        trailerAdapterViewHolder AdapterViewHolder = new trailerAdapterViewHolder(view);
        return AdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(trailerAdapterViewHolder holder, int position)
    {
        String poster = mTrailerList.get(position).getMthumbnailUrl();
        if(poster!=null) {
            //RGB_565 is used for the memory optimization. R plane spends 5 bit per pixel instead of 8 bits. Same applies to other plane.
            Picasso.with(mContext).load(poster).placeholder(R.drawable.backdrop_loading_placeholder)
                    .error(R.drawable.no_image).config(Bitmap.Config.RGB_565).into(holder.mtrailerImage);
        }

    }


    @Override
    public int getItemCount() {
        if(mTrailerList == null)
        {
            return 0;
        }
        return mTrailerList.size();
    }

    public class trailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.trailer_image)
        ImageView mtrailerImage;

        public trailerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String url = mTrailerList.get(adapterPosition).getmURL();
            mlistItemClickListener.onItemClicked(url);
        }
    }

    public void setTrailerListData(ArrayList<Trailer> trailerData)
    {
        mTrailerList = trailerData;
        notifyDataSetChanged();
    }
}
