package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 7/30/17.
 */

public class ReviewDao implements Parcelable {
    public String id ;
    public String author ;
    public String content ;
    public String url ;

    protected ReviewDao(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReviewDao> CREATOR = new Parcelable.Creator<ReviewDao>() {
        @Override
        public ReviewDao createFromParcel(Parcel in) {
            return new ReviewDao(in);
        }

        @Override
        public ReviewDao[] newArray(int size) {
            return new ReviewDao[size];
        }
    };
}

