package com.example.androidzk.projectoneudacity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by androidZK on 6/27/2016.
 */
public class DetailMovieInfo extends BasicMovieInfo {
    private String movieSynopsis;
    private float movieRating;
    private String movieReleaseDate;
    private String movieTitle;

    DetailMovieInfo(String movieTitle,
                    String movieSynopsis,
                    float movieRating,
                    String movieReleaseDate,
                    String completePosterPath,
                    long movieID) {
        super(completePosterPath, movieID);
        this.movieTitle = movieTitle;
        this.movieSynopsis = movieSynopsis;
        this.movieRating = movieRating;
        this.movieReleaseDate = movieReleaseDate;
    }


    DetailMovieInfo(Parcel in) {
        super(in);
        this.movieTitle = in.readString();
        this.movieSynopsis = in.readString();
        this.movieRating = in.readInt();
        this.movieReleaseDate = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(movieTitle);
        dest.writeString(movieSynopsis);
        dest.writeFloat(movieRating);
        dest.writeString(movieReleaseDate);
    }

    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<DetailMovieInfo> CREATOR
            = new Parcelable.Creator<DetailMovieInfo>() {

        public DetailMovieInfo createFromParcel(Parcel in) {
            return new DetailMovieInfo(in);
        }

        public DetailMovieInfo[] newArray(int size) {
            return new DetailMovieInfo[size];
        }
    };

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public float getMovieRating() {
        return movieRating;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public String getMovieTitle() { return this.movieTitle; }

}
