package com.m7md.android.mymovieapp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by m7md on 3/25/16.
 */
public class Movie implements Serializable {
    private String Trailer;

    private int ID;

    private String poster_path;

    private String title;
    private String vote_average;
    private String overview;
    private String minutes;
    private int favourite;

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
}
