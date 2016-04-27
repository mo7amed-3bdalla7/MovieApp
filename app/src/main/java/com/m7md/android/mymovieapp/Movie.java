package com.m7md.android.mymovieapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by m7md on 3/25/16.
 */
public class Movie implements Parcelable {
    private String Trailer;

    private int ID;

    private String poster_path;

    private String title;
    private String vote_average;
    private String overview;
    private String minutes;
    private int favourite;

    public Movie() {
    }

    protected Movie(Parcel in) {
        Trailer = in.readString();
        ID = in.readInt();
        poster_path = in.readString();
        title = in.readString();
        vote_average = in.readString();
        overview = in.readString();
        minutes = in.readString();
        favourite = in.readInt();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getRelease_date() {
        return release_date;
    }


    private String release_date;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {

            parse = format.parse(release_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parse.toString().split(" ")[5];
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public void setTrailer(String trailer) {
        Trailer = trailer;
    }

    public String getTrailer() {
        return Trailer;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Trailer);
        dest.writeInt(ID);
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(overview);
        dest.writeString(minutes);
        dest.writeInt(favourite);
        dest.writeString(release_date);
    }
}
