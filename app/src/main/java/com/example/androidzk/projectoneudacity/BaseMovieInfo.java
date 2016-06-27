package com.example.androidzk.projectoneudacity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by androidZK on 6/27/2016.
 */
public class BaseMovieInfo implements Parcelable{
    private ImageView moviePoster;
    private Bitmap.Config bitMapConfig = Bitmap.Config.ARGB_4444;

    BaseMovieInfo(ImageView imgView) {
        this.moviePoster= imgView;
    }

    BaseMovieInfo(Parcel in){
        Bitmap imgBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.moviePoster.setImageBitmap(imgBitmap);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap imgBitmap = Bitmap.createBitmap(
                moviePoster.getWidth(),moviePoster.getHeight(), bitMapConfig);
        dest.writeValue(imgBitmap);
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

    public ImageView getMoviePoster() {
        return this.moviePoster;
    }

    public void setMoviePoster(ImageView moviePoster) {
        this.moviePoster = moviePoster;
    }
}
