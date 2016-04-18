package com.m7md.android.mymovieapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    MovieDB movieDB;

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
            default:
                taskFactory("popular");
        }

        return true;
    }

    void taskFactory(String type) {
        ArrayList<Movie> movies = null;

        gridView = (GridView) findViewById(R.id.gridView);

        itemsTask itemsTask = new itemsTask();


        itemsTask.execute(type);

        try {


            movies = itemsTask.get();


        } catch (InterruptedException e) {
            Toast.makeText(getBaseContext(),""+e, Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(getBaseContext(), ""+e, Toast.LENGTH_SHORT).show();
        }


        GridAdapter adapter = new GridAdapter(this, movies);

        gridView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDB = new MovieDB(getBaseContext());
        taskFactory("popular");

    }


    class itemsTask extends AsyncTask<String, String, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            if (params[0] == null)
                params[0] = "top_rated";


            if (params[0] == "favourite")
                return fetchMovies(-1);





            String url = "http://api.themoviedb.org/3/movie/" + params[0];
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
                return parseJson(moviesJson);

            } catch (MalformedURLException e) {
                Toast.makeText(getBaseContext(), "error1", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
               return fetchMovies(0);

            }


            return null;
        }



        private ArrayList fetchMovies(int flag){
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
                Toast.makeText(getBaseContext(), "444555888", Toast.LENGTH_SHORT).show();
            }


            return allMovies;
        }

    }

}