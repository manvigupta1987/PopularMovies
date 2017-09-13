package com.example.manvi.movieappstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manvi on 20/3/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final String TAG = TrailerAdapter.class.getSimpleName();
    private ArrayList<Reviews> mReviewList;

    private Context mContext;

    public ReviewAdapter(Context context){
        mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup,false);
        ReviewAdapterViewHolder AdapterViewHolder = new ReviewAdapterViewHolder(view);
        return AdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position)
    {
        String content = mReviewList.get(position).getmContent();
        holder.mReviewText.setText(content);
        String author = mReviewList.get(position).getmAuthor();
        holder.mReviewAuthor.setText(author);
    }


    @Override
    public int getItemCount() {
        if(mReviewList == null)
        {
            return 0;
        }
        return mReviewList.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.Review_Author)
        TextView mReviewAuthor;

        @BindView(R.id.review_text)
        TextView mReviewText;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setReviewListData(ArrayList<Reviews> ReviewData)
    {
        mReviewList = ReviewData;
        notifyDataSetChanged();
    }
}
