package com.example.user.moviedbstage2udacity.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 7/30/17.
 */

public class VideoData implements Parcelable {
    public String id ;
    public String iso_639_1 ;
    public String iso_3166_1 ;
    public String key ;
    public String name ;
    public String site ;
    public int size ;
    public String type ;

    protected VideoData(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoData> CREATOR = new Parcelable.Creator<VideoData>() {
        @Override
        public VideoData createFromParcel(Parcel in) {
            return new VideoData(in);
        }

        @Override
        public VideoData[] newArray(int size) {
            return new VideoData[size];
        }
    };
}

