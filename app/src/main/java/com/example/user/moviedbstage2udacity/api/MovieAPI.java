package com.example.user.moviedbstage2udacity.api;

import com.example.user.moviedbstage2udacity.Constants;
import com.example.user.moviedbstage2udacity.dao.ListMoviesDao;
import com.example.user.moviedbstage2udacity.dao.ListReviewsDao;
import com.example.user.moviedbstage2udacity.dao.ListVideo;
import com.example.user.moviedbstage2udacity.dao.MovieDetailDao;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by user on 7/30/17.
 */

public class MovieAPI {
    public static void requestNowPlaying(Callback<ListMoviesDao> callback){
        Call<ListMoviesDao> call = RetrofitClient.getApiInstance().getApiService().getNowPlaying(Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestUpcoming(Callback<ListMoviesDao> callback){
        Call<ListMoviesDao> call= RetrofitClient.getApiInstance().getApiService().getUpComing(Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestPopular(Callback<ListMoviesDao> callback){
        Call<ListMoviesDao> call = RetrofitClient.getApiInstance().getApiService().getPopular(Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestTopRated(Callback<ListMoviesDao> callback){
        Call<ListMoviesDao> call = RetrofitClient.getApiInstance().getApiService().getTopRated(Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestMovieDetail(int movieId, Callback<MovieDetailDao> callback){
        Call<MovieDetailDao> call = RetrofitClient.getApiInstance().getApiService().getDetailMovie(movieId,Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestListTrailer(int movieId, Callback<ListVideo> callback){
        Call<ListVideo> call = RetrofitClient.getApiInstance().getApiService().getListVideo(movieId,Constants.api_key);
        call.enqueue(callback);
    }

    public static void requestListReviews(int movieId, Callback<ListReviewsDao> callback){
        Call<ListReviewsDao> call = RetrofitClient.getApiInstance().getApiService().getListReview(movieId,Constants.api_key);
        call.enqueue(callback);
    }
}
