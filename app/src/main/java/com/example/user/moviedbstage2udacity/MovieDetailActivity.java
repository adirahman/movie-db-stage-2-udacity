package com.example.user.moviedbstage2udacity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.moviedbstage2udacity.adapter.ReviewAdapter;
import com.example.user.moviedbstage2udacity.adapter.TrailerAdapter;
import com.example.user.moviedbstage2udacity.api.MovieAPI;
import com.example.user.moviedbstage2udacity.dao.ListReviewsDao;
import com.example.user.moviedbstage2udacity.dao.ListVideo;
import com.example.user.moviedbstage2udacity.dao.MovieDao;
import com.example.user.moviedbstage2udacity.dao.ReviewDao;
import com.example.user.moviedbstage2udacity.dao.VideoData;
import com.example.user.moviedbstage2udacity.database.favorite.FavoriteDBHelper;
import com.example.user.moviedbstage2udacity.database.favorite.FavoriteMovieContract;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterListener,ReviewAdapter.ReviewAdapaterListener{

    String TAG = MovieDetailActivity.class.getSimpleName();

    MovieDao dataMovie = new MovieDao();
    ListVideo dataTrailer = new ListVideo();
    ListReviewsDao dataReview = new ListReviewsDao();

    ImageView ivPoster;
    TextView tvTitle,tvDesc,tvVote,tvRelease,tvNoReviews;
    Button btnAddFavorite;
    RecyclerView trailerRecyclerView,reviewRecyclerView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;

    ProgressDialog progressDialog;

    private SQLiteDatabase mDb;
    private static final String MOVIE_DATA = "movieData";
    private static final String TRAILER_DATA = "trailerData";
    private static final String REVIEW_DATA = "reviewData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        progressDialog = new ProgressDialog(this);
        ivPoster = (ImageView) findViewById(R.id.iv_poster);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvVote = (TextView) findViewById(R.id.tv_vote);
        tvRelease = (TextView) findViewById(R.id.tv_release_date);
        tvNoReviews = (TextView) findViewById(R.id.tv_no_reviews);
        btnAddFavorite = (Button) findViewById(R.id.btn_favorite);
        trailerRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.recycler_reviews);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager llm2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reviewRecyclerView.setLayoutManager(llm2);
        trailerRecyclerView.setLayoutManager(llm);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(MOVIE_DATA)){
                dataMovie = savedInstanceState.getParcelable(MOVIE_DATA);
                initView();
            }
            if(savedInstanceState.containsKey(TRAILER_DATA)){
                dataTrailer = savedInstanceState.getParcelable(TRAILER_DATA);
                initViewTrailer(dataTrailer);
            }
            if(savedInstanceState.containsKey(REVIEW_DATA)){
                dataReview = savedInstanceState.getParcelable(REVIEW_DATA);
                initViewReview(dataReview);
            }
        }else{
            getDataFromAPI();
        }
        initDB();
    }

    public static void startThisActivity(Context context, MovieDao movieDao){
        Intent i =new Intent(context,MovieDetailActivity.class);
        i.putExtra("MovieData",movieDao);
        context.startActivity(i);
    }

    private void initView(){
        Picasso.with(MovieDetailActivity.this)
                .load(Constants.BASE_IMAGE_URL_2+dataMovie.backdrop_path)
                .into(ivPoster);
        tvTitle.setText(dataMovie.original_title);
        tvDesc.setText(dataMovie.overview);
        tvVote.setText("Vote Average : "+String.valueOf(dataMovie.vote_average));
        tvRelease.setText("Release Date : "+dataMovie.release_date);
    }

    private void initViewTrailer(ListVideo dataTrailer){
        trailerAdapter = new TrailerAdapter(dataTrailer.results,MovieDetailActivity.this,MovieDetailActivity.this);
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();
    }

    private void initViewReview(ListReviewsDao dataReview){
        if(dataReview.results.size() > 0){
            reviewAdapter = new ReviewAdapter(dataReview.results,MovieDetailActivity.this,MovieDetailActivity.this);
            reviewRecyclerView.setAdapter(reviewAdapter);
            reviewAdapter.notifyDataSetChanged();

            tvNoReviews.setVisibility(View.GONE);
            reviewRecyclerView.setVisibility(View.VISIBLE);
        }else{
            tvNoReviews.setVisibility(View.VISIBLE);
            reviewRecyclerView.setVisibility(View.GONE);
        }
    }

    public void getDataFromAPI(){
        dataMovie = getIntent().getParcelableExtra("MovieData");
        initView();

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait Initializing Detail Movie Progress...");
        progressDialog.show();

        getTrailer();
        getReviews();
    }

    public void getTrailer(){
        MovieAPI.requestListTrailer(dataMovie.id,callBackTrailer);
    }

    Callback<ListVideo> callBackTrailer = new Callback<ListVideo>() {
        @Override
        public void onResponse(Call<ListVideo> call, Response<ListVideo> response) {
            if(response.isSuccessful()){
                dataTrailer = response.body();
                initViewTrailer(dataTrailer);

                Log.d(TAG,"response success");
            }else {
                Log.d(TAG,"response failed");
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ListVideo> call, Throwable t) {
            Log.d(TAG,t.getMessage());
            progressDialog.dismiss();
        }
    };

    private void getReviews(){
        MovieAPI.requestListReviews(dataMovie.id,callbackReviews);
    }

    Callback<ListReviewsDao> callbackReviews = new Callback<ListReviewsDao>() {
        @Override
        public void onResponse(Call<ListReviewsDao> call, Response<ListReviewsDao> response) {
            if(response.isSuccessful()){
                Log.d("Reviews","success");
                dataReview = response.body();
                initViewReview(dataReview);
            }else{
                Log.d("Reviews","failed on response");
            }
        }

        @Override
        public void onFailure(Call<ListReviewsDao> call, Throwable t) {
            Log.d("Reviews","on failure : "+t.getMessage());
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_DATA,dataMovie);
        outState.putParcelable(TRAILER_DATA,dataTrailer);
        outState.putParcelable(REVIEW_DATA,dataReview);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dataMovie = savedInstanceState.getParcelable(MOVIE_DATA);
        dataTrailer = savedInstanceState.getParcelable(TRAILER_DATA);
        dataReview = savedInstanceState.getParcelable(REVIEW_DATA);
    }


    // trailer on Click
    @Override
    public void onClick(VideoData videoData) {
        //Toast.makeText(MovieDetailActivity.this,videoData.key,Toast.LENGTH_LONG).show();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WATCH_TRAILER_URL+videoData.key)));
    }

    // review onClick
    @Override
    public void onClick(ReviewDao reviewData) {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(reviewData.url)));
    }

    private void initDB(){
        FavoriteDBHelper dbHelper = new FavoriteDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }

    public void addFavoriteMovie(View view){
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_ID_MOVIE, dataMovie.id);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,dataMovie.original_title);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_OVERVIEW,dataMovie.overview);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_POSTER_PATH,dataMovie.poster_path);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH,dataMovie.backdrop_path);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,dataMovie.vote_average);
        cv.put(FavoriteMovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,dataMovie.release_date);

        Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteEntry.CONTENT_URI,cv);
        //mDb.insert(FavoriteMovieContract.FavoriteEntry.TABLE_NAME,null,cv);

        if(uri != null){
            Toast.makeText(MovieDetailActivity.this,"Success Add To Favorite",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}

