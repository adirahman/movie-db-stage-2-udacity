package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class ListReviewsDao implements Parcelable {
    public int id ;
    public int page ;
    public List<ReviewDao> results ;
    public int total_pages ;
    public int total_results ;

    public ListReviewsDao(){}
    protected ListReviewsDao(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<ReviewDao>();
            in.readList(results, ReviewDao.class.getClassLoader());
        } else {
            results = null;
        }
        total_pages = in.readInt();
        total_results = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(page);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ListReviewsDao> CREATOR = new Parcelable.Creator<ListReviewsDao>() {
        @Override
        public ListReviewsDao createFromParcel(Parcel in) {
            return new ListReviewsDao(in);
        }

        @Override
        public ListReviewsDao[] newArray(int size) {
            return new ListReviewsDao[size];
        }
    };
}

