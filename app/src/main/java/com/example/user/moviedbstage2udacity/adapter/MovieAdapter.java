package com.example.user.moviedbstage2udacity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.moviedbstage2udacity.Constants;
import com.example.user.moviedbstage2udacity.R;
import com.example.user.moviedbstage2udacity.dao.MovieDao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieVh>{

    private List<MovieDao> listMovies =new ArrayList<>();
    private Context context;
    private MoviewAdapterListener listener;

    public MovieAdapter(List<MovieDao> listMovies, Context context, MoviewAdapterListener listener) {
        this.listMovies = listMovies;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MovieVh onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movie,parent,false);
        return new MovieVh(v);
    }

    @Override
    public void onBindViewHolder(MovieVh holder, int position) {
        holder.bind(listMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    class MovieVh extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout llRow;
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvDescription;

        MovieDao itemMovie;

        public MovieVh(View itemView){
            super(itemView);
            llRow = (LinearLayout) itemView.findViewById(R.id.ll_row);
            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            tvTitle = (TextView)itemView.findViewById(R.id.txt_name);
            tvDescription = (TextView) itemView.findViewById(R.id.txt_desc);
            itemView.setOnClickListener(this);
        }

        void bind(MovieDao item){
            itemMovie = item;
            Picasso.with(context)
                    .load(Constants.BASE_IMAGE_URL+item.poster_path)
                    .into(ivImg);
            tvTitle.setText(item.original_title);
            tvDescription.setText(item.overview);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemMovie);
        }
    }

    public interface MoviewAdapterListener{
        void onClick(MovieDao movieDao);
    }
}

