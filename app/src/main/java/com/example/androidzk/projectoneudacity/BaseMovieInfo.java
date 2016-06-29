package com.example.androidzk.projectoneudacity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by androidZK on 6/27/2016.
 */
public class BaseMovieInfo implements Parcelable{
    private String completePosterPath;
    private long movieID;
    //private Bitmap.Config bitMapConfig = Bitmap.Config.ARGB_4444;

    BaseMovieInfo(String posterPath, long movieID) {
        this.completePosterPath= posterPath;
        this.movieID = movieID;
    }

    BaseMovieInfo(Parcel in){
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

    static final Parcelable.Creator<BaseMovieInfo> CREATOR
            = new Parcelable.Creator<BaseMovieInfo>() {

        public BaseMovieInfo createFromParcel(Parcel in) {
            return new BaseMovieInfo(in);
        }

        public BaseMovieInfo[] newArray(int size) {
            return new BaseMovieInfo[size];
        }
    };

    public String getMoviePosterAddress() {
        return this.completePosterPath;
    }
    public long getMovieTitle() {return this.movieID;}
}
