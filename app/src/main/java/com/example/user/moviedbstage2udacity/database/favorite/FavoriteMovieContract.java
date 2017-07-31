package com.example.user.moviedbstage2udacity.database.favorite;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user on 7/30/17.
 */

public class FavoriteMovieContract {

    public static final String AUTHORITY = "com.example.user.moviedbstage2udacity";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FAVORITE = "favorite";

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_ID_MOVIE = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
