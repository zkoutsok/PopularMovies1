package com.example.androidzk.projectoneudacity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by androidZK on 6/27/2016.
 */
public class BasicMovieInfo implements Parcelable{
    private String completePosterPath;
    private long movieID;
    //private Bitmap.Config bitMapConfig = Bitmap.Config.ARGB_4444;

    BasicMovieInfo(String posterPath, long movieID) {
        this.completePosterPath= posterPath;
        this.movieID = movieID;
    }

    BasicMovieInfo(Parcel in){
       // Bitmap imgBitmap = in.readParcelable(Bitmap.class.getClassLoader());
       // this.posterPath.setImageBitmap(imgBitmap);
        this.completePosterPath = in.readString();
        this.movieID = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       // Bitmap imgBitmap = Bitmap.createBitmap(
           //     moviePoster.getWidth(),moviePoster.getHeight(), bitMapConfig);
        dest.writeString(completePosterPath);
        dest.writeLong(movieID);
    }

    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<BasicMovieInfo> CREATOR
            = new Parcelable.Creator<BasicMovieInfo>() {

        public BasicMovieInfo createFromParcel(Parcel in) {
            return new BasicMovieInfo(in);
        }

        public BasicMovieInfo[] newArray(int size) {
            return new BasicMovieInfo[size];
        }
    };

    public String getMoviePosterAddress() {
        return this.completePosterPath;
    }
    public long getMovieID() {return this.movieID;}
}
