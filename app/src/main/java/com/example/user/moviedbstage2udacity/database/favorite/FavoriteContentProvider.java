package com.example.user.moviedbstage2udacity.database.favorite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by user on 7/30/17.
 */

public class FavoriteContentProvider extends ContentProvider {

    public static final int FAVORITE = 100;
    /*public static final int FAVORITE_WITH_ID = 101;*/
    public static final int FAVORITE_WITH_TITLE = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE, FAVORITE);
        /*uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,FavoriteMovieContract.PATH_FAVORITE+"/#",FAVORITE_WITH_ID);*/
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE + "/*", FAVORITE_WITH_TITLE);

        return uriMatcher;
    }

    private FavoriteDBHelper mFavoriteDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case FAVORITE:
                retCursor = db.query(FavoriteMovieContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITE:
                long id = db.insert(FavoriteMovieContract.FavoriteEntry.TABLE_NAME,null,values);
                if(id > 0){
                    //success
                    returnUri = ContentUris.withAppendedId(FavoriteMovieContract.FavoriteEntry.CONTENT_URI,id);
                }else{
                    throw new android.database.SQLException("Failed insert row into "+uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

