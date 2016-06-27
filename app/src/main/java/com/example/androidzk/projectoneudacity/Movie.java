package com.example.androidzk.projectoneudacity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by androidZK on 6/27/2016.
 */
public class Movie extends BaseMovieInfo {
    private String movieTitle;
    private String movieSynopsis;
    private int movieRating;
    private Date movieReleaseDate;

    Movie(Parcel in) {
        super(in);
        this.movieTitle = in.readString();
        this.movieSynopsis = in.readString();
        this.movieRating = in.readInt();
        this.movieReleaseDate = new Date(in.readLong());
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(movieTitle);
        dest.writeString(movieSynopsis);
        dest.writeInt(movieRating);
        dest.writeLong(movieReleaseDate.getTime());
    }

    public int describeContents() {
        return 0;
    }

    static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Date getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public int getMovieRating() {
        return movieRating;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }

    public void setMovieRating(int movieRating) {
        this.movieRating = movieRating;
    }

    public void setMovieReleaseDate(Date movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }


}
