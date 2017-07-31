package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class ListMoviesDao implements Parcelable {
    public int page ;
    public List<MovieDao> results = new ArrayList<>();
    public DatesDao dates ;
    public int total_pages ;
    public int total_results ;

    public ListMoviesDao() {
    }

    public ListMoviesDao(int page, List<MovieDao> results, DatesDao dates, int total_pages, int total_results) {
        this.page = page;
        this.results = results;
        this.dates = dates;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    protected ListMoviesDao(Parcel in) {
        page = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<MovieDao>();
            in.readList(results, MovieDao.class.getClassLoader());
        } else {
            results = null;
        }
        dates = (DatesDao) in.readValue(DatesDao.class.getClassLoader());
        total_pages = in.readInt();
        total_results = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeValue(dates);
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ListMoviesDao> CREATOR = new Parcelable.Creator<ListMoviesDao>() {
        @Override
        public ListMoviesDao createFromParcel(Parcel in) {
            return new ListMoviesDao(in);
        }

        @Override
        public ListMoviesDao[] newArray(int size) {
            return new ListMoviesDao[size];
        }
    };
}
