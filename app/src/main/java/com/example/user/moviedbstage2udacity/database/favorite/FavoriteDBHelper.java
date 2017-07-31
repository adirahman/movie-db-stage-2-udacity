package com.example.user.moviedbstage2udacity.database.favorite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 7/30/17.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "favorite.db";

    static final int DATABASE_VERSION = 1;

    public FavoriteDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE "+
                FavoriteMovieContract.FavoriteEntry.TABLE_NAME + " ("+
                FavoriteMovieContract.FavoriteEntry.COLUMN_ID_MOVIE + " INTEGER NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL,"+
                FavoriteMovieContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteMovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}

