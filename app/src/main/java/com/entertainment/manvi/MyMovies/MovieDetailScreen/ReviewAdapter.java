package com.entertainment.manvi.MyMovies.MovieDetailScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entertainment.manvi.MyMovies.data.Reviews;
import com.entertainment.manvi.MyMovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<Reviews> mReviewList;

    public ReviewAdapter(){
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup,false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position)
    {
        String content = mReviewList.get(position).getmContent();
        holder.mReviewText.setText(content);
        holder.mReviewText.setContentDescription(content);
        String author = mReviewList.get(position).getmAuthor();
        holder.mReviewAuthor.setText(author);
        holder.mReviewAuthor.setContentDescription(author);
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
