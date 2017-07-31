package com.example.user.moviedbstage2udacity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.moviedbstage2udacity.R;
import com.example.user.moviedbstage2udacity.dao.ReviewDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewVh>{
    private List<ReviewDao> listReview = new ArrayList<>();
    private Context context;
    private ReviewAdapaterListener listener;

    public ReviewAdapter(List<ReviewDao> listReview, Context context, ReviewAdapaterListener listener) {
        this.listReview = listReview;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ReviewVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reviews,parent,false);
        return new ReviewVh(v);
    }

    @Override
    public void onBindViewHolder(ReviewVh holder, int position) {
        holder.bind(listReview.get(position));
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    class ReviewVh extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvAuthor;
        private TextView tvContent;
        ReviewDao itemReview;

        public ReviewVh(View itemView){
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(this);
        }

        void bind(ReviewDao item){
            itemReview = item;
            tvAuthor.setText(item.author);
            tvContent.setText(item.content);
        }
        @Override
        public void onClick(View v) {
            listener.onClick(itemReview);
        }
    }

    public interface ReviewAdapaterListener{
        void onClick(ReviewDao reviewData);
    }
}

