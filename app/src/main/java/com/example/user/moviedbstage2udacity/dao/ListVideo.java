package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/30/17.
 */

public class ListVideo implements Parcelable {
    public int id;
    public List<VideoData> results;

    public ListVideo(){}
    protected ListVideo(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<VideoData>();
            in.readList(results, VideoData.class.getClassLoader());
        } else {
            results = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ListVideo> CREATOR = new Parcelable.Creator<ListVideo>() {
        @Override
        public ListVideo createFromParcel(Parcel in) {
            return new ListVideo(in);
        }

        @Override
        public ListVideo[] newArray(int size) {
            return new ListVideo[size];
        }
    };
}

