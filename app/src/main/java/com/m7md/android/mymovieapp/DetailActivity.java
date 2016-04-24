package com.m7md.android.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {

    private static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        android.app.FragmentManager fm = getFragmentManager();
        android.app.Fragment fragment = fm.findFragmentById(R.id.frame);

        if (fragment == null) {
            fragment = new DetailFragment();
            fm.beginTransaction()
                    .add(R.id.frame, fragment)
                    .commit();
        }


    }


    public static Intent setIntent(Context context, Movie movie) {

        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(MOVIE, movie);


        return intent;
    }


}
