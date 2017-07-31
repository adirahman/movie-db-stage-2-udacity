package com.example.user.moviedbstage2udacity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.user.moviedbstage2udacity.adapter.MovieAdapter;
import com.example.user.moviedbstage2udacity.dao.ListMoviesDao;
import com.example.user.moviedbstage2udacity.dao.MovieDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class MovieFragment extends Fragment implements MovieAdapter.MoviewAdapterListener{
    int color;
    ListMoviesDao data = new ListMoviesDao();
    MoviePassInterface communicator;

    private static final String LIST_MOVIES = "onSavedSate";
    List<MovieDao> listMovies = new ArrayList<>();
    RecyclerView recyclerView;

    public MovieFragment() {
        // Required empty public constructor
    }

    /*@SuppressLint("ValidFragment")
    public MovieFragment(ListMoviesDao data){
        this.data = data;
        listMovies.addAll(data.results);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (MoviePassInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie, container, false);
        data = getArguments().getParcelable(MainActivity.BUNDLE_MOVIE_DATA);

        final FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.fl);
        frameLayout.setBackgroundColor(color);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        detectOrientationScreen();

        setData();

        return v;
    }

    public void setData(){
        MovieAdapter adapter = new MovieAdapter(data.results,getContext(),this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void detectOrientationScreen(){
        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
        }else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setHasFixedSize(true);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_MOVIES,data);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState !=null){
            data = savedInstanceState.getParcelable(LIST_MOVIES);
        }

    }

    @Override
    public void onClick(MovieDao movieDao) {
        communicator.passMoviewData(movieDao);
    }
}

