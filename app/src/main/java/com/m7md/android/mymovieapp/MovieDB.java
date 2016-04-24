package com.m7md.android.mymovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by m7md on 4/16/16.
 */
public class MovieDB extends SQLiteOpenHelper {
    final static int DB_VERSION = 2;
    final static String DB_NAME = "movie";

    public MovieDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //AUTOINCREMENT

        db.execSQL("create table movie (id integer primary key ," +
                "poster_path varchar(100)," +
                "title  varchar(100)," +
                "vote_average  varchar(100)," +
                "overview text," +
                "minutes varchar(100)," +
                "trailer varchar(100)," +
                "favourite integer(1)," +
                "release_date varchar(100));");

    }

    public int updateFavourite(int id) {


        ContentValues movieDetails = new ContentValues();
        movieDetails.put("favourite", 1);

        return getWritableDatabase().update("movie", movieDetails, "id=" + id, null);
    }

    public int updateMovie(ContentValues movieDetails) {
        return getWritableDatabase().update("movie", movieDetails, "id=" + movieDetails.getAsInteger("id"), null);
    }

    public long insertMovie(Movie movie) {


        ContentValues movieDetails = new ContentValues();
        movieDetails.put("id", movie.getID());
        movieDetails.put("poster_path", movie.getPoster_path());
        movieDetails.put("title", movie.getTitle());
        movieDetails.put("vote_average", movie.getVote_average());
        movieDetails.put("overview", movie.getOverview());
        movieDetails.put("minutes", movie.getMinutes());
        movieDetails.put("release_date", movie.getRelease_date());

        return getWritableDatabase().insert("movie", null, movieDetails);
    }


    public Cursor selectMovie(int id) {
        if (id == 0) {
            return getWritableDatabase().rawQuery("select * from movie", null);
        } else if (id == -1) {
            return getWritableDatabase().rawQuery("select * from movie where favourite=1", null);
        } else if (id == -2) {
            return getWritableDatabase().rawQuery("select * from movie limit 1", null);
        } else {
            return getWritableDatabase().rawQuery("select * from movie where id =" + id, null);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table movie");
        onCreate(db);

    }
}
