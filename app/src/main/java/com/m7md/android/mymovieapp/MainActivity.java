package com.m7md.android.mymovieapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    static boolean landscape;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.popular:
                taskFactory("popular");
                break;
            case R.id.top_rated:
                taskFactory("top_rated");
                break;
            case R.id.favourite:
                taskFactory("favourite");
                break;
            case R.id.menu_item_share:
                setShareIntent();
                break;
            default:
                taskFactory("popular");
        }

        return true;
    }

    private void setShareIntent() {


        Cursor cursor = new MovieDB(this).selectMovie(-2);
        cursor.moveToFirst();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        /*
         "This A " + cursor.getString(cursor.getColumnIndex("title")) + " Movie Details \n" +
                "Overview :  " + cursor.getString(cursor.getColumnIndex("overview"));
         */
        String shareBody = "https://wwww.youtube.com/?v=" + cursor.getString(cursor.getColumnIndex("trailer"));
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movie Url");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    void taskFactory(String type) {
        ArrayList<Movie> movies = null;

        gridView = (GridView) findViewById(R.id.gridView);

        ItemsTask itemsTask = new ItemsTask();
        itemsTask.setContext(getBaseContext());


        itemsTask.execute(type, "");

        try {


            movies = itemsTask.get();


        } catch (InterruptedException e) {
            Toast.makeText(getBaseContext(), "" + e, Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(getBaseContext(), "" + e, Toast.LENGTH_SHORT).show();
        }


        GridAdapter adapter = new GridAdapter(this, movies);

        gridView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        landscape = checkShow();
        if (landscape) {
            setContentView(R.layout.fragment_activity);
            Intent intent = getIntent();
            Movie movie = (Movie) intent.getSerializableExtra("movie");
            if (movie == null) {
                View view = findViewById(R.id.detailFragment);
                view.setVisibility(View.INVISIBLE);
            } else {
                View view = findViewById(R.id.detailFragment);
                view.setVisibility(View.VISIBLE);
            }
        } else {
            setContentView(R.layout.activity_main);
        }
        taskFactory("popular");

    }


    public boolean checkShow() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        return (width > height);
    }


}