package com.example.user.moviedbstage2udacity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.moviedbstage2udacity.adapter.MainViewPagerAdapter;
import com.example.user.moviedbstage2udacity.api.MovieAPI;
import com.example.user.moviedbstage2udacity.dao.DatesDao;
import com.example.user.moviedbstage2udacity.dao.ListMoviesDao;
import com.example.user.moviedbstage2udacity.dao.MovieDao;
import com.example.user.moviedbstage2udacity.database.favorite.FavoriteDBHelper;
import com.example.user.moviedbstage2udacity.database.favorite.FavoriteMovieContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviePassInterface {

    String TAG = MainActivity.class.getSimpleName();
    ViewPager viewPager;
    CoordinatorLayout mainLayout;
    ProgressBar pb;
    ListMoviesDao nowPlaying = new ListMoviesDao();
    ListMoviesDao listUpComing = new ListMoviesDao();
    ListMoviesDao listPopular = new ListMoviesDao();
    ListMoviesDao listTopRated = new ListMoviesDao();
    ListMoviesDao listFavorite = new ListMoviesDao();
    ProgressDialog progressDialog;

    public static final String BUNDLE_MOVIE_DATA = "bundle_movie_data";

    private static final String NOW_PLAYING = "nowPlaying";
    private static final String LIST_UP_COMING = "listUpComing";
    private static final String LIST_POPULAR = "listPopular";
    private static final String LIST_TOP_RATED = "listTopRated";
    private static final String LIST_FAVORITE = "listFavorite";

    int flag = 0;
    int getPositionTab = 0;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        mainLayout = (CoordinatorLayout) findViewById(R.id.htab_main_content);
        pb = (ProgressBar) findViewById(R.id.loading);
        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);

        initDB();
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(NOW_PLAYING)){
                nowPlaying = savedInstanceState.getParcelable(NOW_PLAYING);
            }
            if(savedInstanceState.containsKey(LIST_UP_COMING)){
                listUpComing =savedInstanceState.getParcelable(LIST_UP_COMING);
            }
            if(savedInstanceState.containsKey(LIST_POPULAR)){
                listPopular = savedInstanceState.getParcelable(LIST_POPULAR);
            }
            if(savedInstanceState.containsKey(LIST_TOP_RATED)){
                listTopRated = savedInstanceState.getParcelable(LIST_TOP_RATED);
            }
            if(savedInstanceState.containsKey(LIST_FAVORITE)){
                listFavorite = savedInstanceState.getParcelable(LIST_FAVORITE);
            }
            initView();
        }else {
            fetchDataFromAPI();
        }
    }

    public void fetchDataFromAPI(){
        progressDialog.setMessage("Please Wait Fetching Movie Progress...");
        progressDialog.show();
        getNowPlaying();
    }

    public void getNowPlaying(){
        MovieAPI.requestNowPlaying(movieListener);
    }

    public void getUpComing(){
        MovieAPI.requestUpcoming(movieListener);
    }

    public void getPopular(){
        MovieAPI.requestPopular(movieListener);
    }

    public void getTopRated(){
        MovieAPI.requestTopRated(movieListener);
    }

    Callback<ListMoviesDao> movieListener = new Callback<ListMoviesDao>() {
        @Override
        public void onResponse(Call<ListMoviesDao> call, Response<ListMoviesDao> response) {
            if(flag == 0){
                nowPlaying = response.body();
                flag = 1;
                getUpComing();
            }else if(flag == 1){
                listUpComing = response.body();
                flag = 2;
                getPopular();
            }else if(flag == 2){
                listPopular = response.body();
                flag = 3;
                getTopRated();
            }else if(flag == 3){
                listTopRated = response.body();
                flag = 0;
                initView();
            }
        }

        @Override
        public void onFailure(Call<ListMoviesDao> call, Throwable t) {
            Log.e(TAG,t.getMessage());
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this,"Cannot fetch data",Toast.LENGTH_LONG).show();
        }
    };

    public void initView(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(),R.color.white_70));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle("Movie DB Stage 2");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final ImageView imgHeader = (ImageView) findViewById(R.id.htab_header);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);

        try{
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.now_playing);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(getBaseContext(),R.color.primary_500));
                    int vibrantDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(getBaseContext(),R.color.primary_700));
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });
        }catch (Exception e){
            Log.e(TAG,"onCreate : failed to create bitmap from background",e.fillInStackTrace());
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,R.color.primary_500));
            collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this,R.color.primary_700));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                getPositionTab = tab.getPosition();
                int pos = tab.getPosition();
                if(pos == 0){
                    imgHeader.setImageResource(R.drawable.now_playing);
                }else if(pos == 1){
                    imgHeader.setImageResource(R.drawable.up_coming);
                }else if(pos == 2){
                    imgHeader.setImageResource(R.drawable.popular);
                }else if(pos == 3){
                    imgHeader.setImageResource(R.drawable.top_rated);
                }else if(pos == 4){
                    imgHeader.setImageResource(R.drawable.favorite);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        progressDialog.dismiss();
    }
    private void setupViewPager(ViewPager pager){
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());


        MovieFragment movieFragment = new MovieFragment();
        Bundle bundleData = new Bundle();
        bundleData.putParcelable(BUNDLE_MOVIE_DATA,nowPlaying);
        movieFragment.setArguments(bundleData);
        adapter.addFrag(movieFragment,"Now Playing");

        bundleData = new Bundle();
        movieFragment = new MovieFragment();
        bundleData.putParcelable(BUNDLE_MOVIE_DATA,listUpComing);
        movieFragment.setArguments(bundleData);
        adapter.addFrag(movieFragment,"Up Coming");

        bundleData = new Bundle();
        movieFragment = new MovieFragment();
        bundleData.putParcelable(BUNDLE_MOVIE_DATA,listPopular);
        movieFragment.setArguments(bundleData);
        adapter.addFrag(movieFragment,"Popular");

        bundleData = new Bundle();
        movieFragment = new MovieFragment();
        bundleData.putParcelable(BUNDLE_MOVIE_DATA,listTopRated);
        movieFragment.setArguments(bundleData);
        adapter.addFrag(movieFragment,"Top Rated");

        bundleData = new Bundle();
        movieFragment = new MovieFragment();
        bundleData.putParcelable(BUNDLE_MOVIE_DATA,listFavorite);
        movieFragment.setArguments(bundleData);
        adapter.addFrag(movieFragment,"Favorite");

        pager.setAdapter(adapter);
        int a = adapter.getCount();
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @Override
    public void passMoviewData(MovieDao movieDao) {
        MovieDetailActivity.startThisActivity(MainActivity.this,movieDao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(NOW_PLAYING,nowPlaying);
        outState.putParcelable(LIST_POPULAR,listPopular);
        outState.putParcelable(LIST_TOP_RATED,listTopRated);
        outState.putParcelable(LIST_UP_COMING,listUpComing);
        outState.putParcelable(LIST_FAVORITE,listFavorite);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nowPlaying = savedInstanceState.getParcelable(NOW_PLAYING);
        listPopular = savedInstanceState.getParcelable(LIST_POPULAR);
        listTopRated = savedInstanceState.getParcelable(LIST_TOP_RATED);
        listUpComing = savedInstanceState.getParcelable(LIST_UP_COMING);
        listFavorite = savedInstanceState.getParcelable(LIST_FAVORITE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_refresh:
                fetchDataFromAPI();
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Refresh data movie db...");
                return true;
            case R.id.action_favorite:

        }
        return super.onOptionsItemSelected(item);
    }

    private void initDB(){
        FavoriteDBHelper dbHelper = new FavoriteDBHelper(this);
        mDb = dbHelper.getReadableDatabase();
        Cursor cursor = getAllFavorite();
        Log.d("DB Movie",cursor.getCount()+"");
        listFavorite = populateWholeMovie(cursor);
        Toast.makeText(MainActivity.this,cursor.getCount()+"",Toast.LENGTH_LONG).show();
    }


    public Cursor getAllFavorite(){
        return getContentResolver().query(FavoriteMovieContract.FavoriteEntry.CONTENT_URI,null,null,null, FavoriteMovieContract.FavoriteEntry.COLUMN_TIMESTAMP);
    }

    private ListMoviesDao populateWholeMovie(Cursor cursor){
        int page = 1;
        DatesDao dates = new DatesDao("0","1");
        int total_pages = 1;
        int total_results = 1;
        List<MovieDao> results = new ArrayList<>();
        results.addAll(populateMovies(cursor));

        return new ListMoviesDao(page,results,dates,total_pages,total_results);
    }

    private List<MovieDao> populateMovies(Cursor cursor){
        List<MovieDao> movies = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            MovieDao a = new MovieDao();
            a.id = cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_ID_MOVIE));
            a.original_title = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE));
            a.overview = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_OVERVIEW));
            a.poster_path = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_POSTER_PATH));
            a.backdrop_path = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH));
            a.vote_average = cursor.getDouble(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE));
            a.release_date = cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteEntry.COLUMN_RELEASE_DATE));

            movies.add(a);
        }

        return movies;
    }
}

