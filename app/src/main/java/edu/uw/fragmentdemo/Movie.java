package edu.uw.fragmentdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class that represents information about a movie.
 */
public class Movie implements Parcelable {
    public String title;
    public String year;
    public String description;
    public String url;

    public Movie(String title, String year, String description, String url) {
        this.title = title;
        this.year = year.substring(0,4);
        this.description = description;
        this.url = url;
    }

    //default constructor; empty movie
    public Movie(){}

    public String toString() { return this.title + " (" + this.year +")"; }

    protected Movie(Parcel in) {
        title = in.readString();
        year = in.readString();
        description = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(year);
        dest.writeString(description);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}