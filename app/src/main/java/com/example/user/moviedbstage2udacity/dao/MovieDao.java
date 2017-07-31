package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 7/30/17.
 */

public class MovieDao implements Parcelable {
    public String poster_path;
    public boolean adult;
    public String overview;
    public String release_date;
    public int[] genre_ids;
    public int id;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public double popularity;
    public int vote_count;
    public boolean video;
    public double vote_average;


    public MovieDao() {
    }

    protected MovieDao(Parcel in) {
        poster_path = in.readString();
        adult = in.readByte() != 0x00;
        overview = in.readString();
        release_date = in.readString();
        id = in.readInt();
        original_title = in.readString();
        original_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        video = in.readByte() != 0x00;
        vote_average = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeDouble(vote_average);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieDao> CREATOR = new Parcelable.Creator<MovieDao>() {
        @Override
        public MovieDao createFromParcel(Parcel in) {
            return new MovieDao(in);
        }

        @Override
        public MovieDao[] newArray(int size) {
            return new MovieDao[size];
        }
    };
}

