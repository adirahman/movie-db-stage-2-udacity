package com.example.user.moviedbstage2udacity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.moviedbstage2udacity.R;
import com.example.user.moviedbstage2udacity.dao.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerVh>{
    private List<VideoData> listVideo = new ArrayList<>();
    private Context context;
    private TrailerAdapterListener listener;

    public TrailerAdapter(List<VideoData> listVideo, Context context, TrailerAdapterListener listener) {
        this.listVideo = listVideo;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public TrailerVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trailer,parent,false);
        return new TrailerVh(v);
    }

    @Override
    public void onBindViewHolder(TrailerVh holder, int position) {
        holder.bind(listVideo.get(position));
    }

    @Override
    public int getItemCount() {
        return listVideo.size();
    }

    class TrailerVh extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        VideoData itemVideo;

        public TrailerVh(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        void bind(VideoData item){
            itemVideo = item;
            tvTitle.setText(item.name);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemVideo);
        }
    }

    public interface TrailerAdapterListener{
        void onClick(VideoData videoData);
    }
}

