package com.m7md.android.mymovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements DetailFragment.OnItemSelectedListener {
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
        String shareBody = "This A " + cursor.getString(cursor.getColumnIndex("title")) + " Movie Details \n" +
                "Overview :  " + cursor.getString(cursor.getColumnIndex("overview"));
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    void taskFactory(String type) {
        ArrayList<Movie> movies = null;

        gridView = (GridView) findViewById(R.id.gridView);

        itemsTask itemsTask = new itemsTask();
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

    @Override
    public void onRssItemSelected(String link) {
        DetailFragment fragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
        fragment.displayFrame();
    }


    public static class itemsTask extends AsyncTask<String, String, ArrayList<Movie>> {
        MovieDB movieDB;

        Context context;

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            movieDB = new MovieDB(context);

            String url = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            if (params[0] == null)
                params[0] = "top_rated";


            if (params[0] == "favourite")
                return fetchMovies(-1);


            if (params[1] == "trailer") {
                url = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";

            } else {

                url = "http://api.themoviedb.org/3/movie/" + params[0];
            }
            String moviesJson;

            try {
                Uri.Builder builder = new Uri.Builder();

                builder.appendQueryParameter("api_key", BuildConfig.MOVIE_API_KEY);
                URL apiUrl = new URL(url.concat(builder.toString()));
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

//                new MovieDB(getBaseContext()).getWritableDatabase().rawQuery("TRUNCATE table movie", null);

                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJson = buffer.toString();
                if (params[1] == "trailer") {
                    ;
                    return parseTrailer(moviesJson, Integer.parseInt(params[0]));

                }
                return parseJson(moviesJson);

            } catch (MalformedURLException e) {
                Toast.makeText(context, "error1", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                return fetchMovies(0);

            }


            return null;
        }

        private ArrayList fetchMovies(int flag) {
            Cursor cursor = movieDB.selectMovie(flag);

            ArrayList<Movie> allMovies = new ArrayList<>();

            try {
                while (cursor.moveToNext()) {

                    Movie movie = new Movie();

                    movie.setID(cursor.getInt(cursor.getColumnIndex("id")));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    movie.setVote_average(cursor.getString(cursor.getColumnIndex("vote_average")));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
                    movie.setPoster_path(cursor.getString(cursor.getColumnIndex("poster_path")));
                    movie.setRelease_date(cursor.getString(cursor.getColumnIndex("release_date")));
                    movie.setMinutes(cursor.getString(cursor.getColumnIndex("minutes")));
                    movie.setTrailer(cursor.getString(cursor.getColumnIndex("trailer")));


                    allMovies.add(movie);
                }
            } finally {
                cursor.close();
            }
            return allMovies;
        }


        private ArrayList<Movie> parseJson(String jsonString) {
            ArrayList<Movie> allMovies = new ArrayList<>();

            try {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesList = moviesObject.getJSONArray("results");

                for (int i = 0; i < moviesList.length(); i++) {

                    JSONObject movieObject = moviesList.getJSONObject(i);

                    Movie movie = new Movie();

                    movie.setID(Integer.parseInt(movieObject.getString("id")));
                    movie.setTitle(movieObject.getString("title"));
                    movie.setVote_average(movieObject.getString("vote_average"));
                    movie.setOverview(movieObject.getString("overview"));
                    movie.setPoster_path(movieObject.getString("poster_path"));
                    movie.setRelease_date(movieObject.getString("release_date"));
                    movie.setMinutes(movieObject.getString("vote_count"));


                    Cursor cursor = movieDB.selectMovie(movie.getID());
                    if (cursor.getCount() == 1) {
//                        Toast.makeText(getBaseContext(), " Founded", Toast.LENGTH_SHORT).show();
                    } else {

                        long inserted = movieDB.insertMovie(movie);
//                        Toast.makeText(getBaseContext(), " inserted" + inserted, Toast.LENGTH_SHORT).show();
                    }

                    allMovies.add(movie);


                }


            } catch (JSONException e) {
                Toast.makeText(context, "Testttttttttttt", Toast.LENGTH_SHORT).show();
            }


            return allMovies;
        }


        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }


        private ArrayList<Movie> parseTrailer(String jsonString, int id) {
            ArrayList<Movie> movies = new ArrayList<>();
            Movie movie;

            try {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray resultArray = moviesObject.getJSONArray("results");

                for (int i = 0; i < resultArray.length(); i++) {
                    movie = new Movie();
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    movie.setTrailer(jsonObject.getString("key"));
                    Cursor cursor = movieDB.selectMovie(id);
                    if (cursor.getCount() == 1) {
                        ContentValues movieDetails = new ContentValues();
                        movieDetails.put("id", id);
                        movieDetails.put("trailer", movie.getTrailer());
//
                        long inserted = movieDB.updateMovie(movieDetails);
                    }
                    movies.add(movie);
                }


            } catch (JSONException e) {
                Toast.makeText(context, e + "", Toast.LENGTH_SHORT).show();
            }


            return movies;
        }


    }

}