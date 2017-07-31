package com.example.user.moviedbstage2udacity.api;

import com.example.user.moviedbstage2udacity.dao.ListMoviesDao;
import com.example.user.moviedbstage2udacity.dao.ListReviewsDao;
import com.example.user.moviedbstage2udacity.dao.ListVideo;
import com.example.user.moviedbstage2udacity.dao.MovieDetailDao;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by user on 7/30/17.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("movie/now_playing")
    Call<ListMoviesDao> getNowPlaying(@Field("api_key") String apiKey);

    @FormUrlEncoded
    @POST("movie/upcoming")
    Call<ListMoviesDao> getUpComing(@Field("api_key") String apiKey);

    @FormUrlEncoded
    @POST("movie/popular")
    Call<ListMoviesDao> getPopular(@Field("api_key") String apiKey);

    @FormUrlEncoded
    @POST("movie/top_rated")
    Call<ListMoviesDao> getTopRated(@Field("api_key") String apiKey);

    @FormUrlEncoded
    @POST("movie/{id}")
    Call<MovieDetailDao> getDetailMovie(@Path("id")int movieId, @Field("api_key") String apiKey);

    @GET("movie/{id}/videos?")
    Call<ListVideo> getListVideo(@Path("id")int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews?")
    Call<ListReviewsDao> getListReview(@Path("id")int movieId, @Query("api_key") String apiKey);
}
