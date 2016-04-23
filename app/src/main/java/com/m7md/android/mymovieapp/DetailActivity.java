package com.m7md.android.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {
    TextView movieTitle, movieDate, movieDeuration, movieRate, overView;
    Movie movie;
    ListView trailerList;
    ArrayList<Movie> movies;

    Button favourite;
    LinearLayout trailer1, trailer2;
    private static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String baseUrl = "http://image.tmdb.org/t/p/w185/";

        ImageView moviePoster = (ImageView) findViewById(R.id.movie_poster);

        movieTitle = (TextView) findViewById(R.id.movie_title);
        movieDate = (TextView) findViewById(R.id.movie_date);
        movieDeuration = (TextView) findViewById(R.id.movie_minuts);
        movieRate = (TextView) findViewById(R.id.movie_rate);
        overView = (TextView) findViewById(R.id.overview);
        favourite = (Button) findViewById(R.id.fav_btn);
      /*  trailer1 = (LinearLayout) findViewById(R.id.trailer1);
        trailer2 = (LinearLayout) findViewById(R.id.trailer2);*/
        trailerList = (ListView) findViewById(R.id.trailer_list);


        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra(MOVIE);


        Picasso.with(getBaseContext()).load(baseUrl + movie.getPoster_path()).into(moviePoster);
        movieTitle.setText(movie.getTitle());
        movieDate.setText(movie.formatDate());
        movieDeuration.setText(movie.getMinutes() + " Min");
        movieRate.setText(movie.getVote_average() + "/10");
        overView.setText(movie.getOverview());

        MainActivity.itemsTask itemsTask = new MainActivity.itemsTask();
        itemsTask.setContext(getBaseContext());
        itemsTask.execute(movie.getID() + "", "trailer");
        try {
            movies = itemsTask.get();
            movie.setTrailer(movies.get(0).getTrailer());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        trailerAdapter listAdapter = new trailerAdapter(this, movies);


        trailerList.setAdapter(listAdapter);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDB movieDB = new MovieDB(getBaseContext());

                int updated = movieDB.updateFavourite(movie.getID());
                if (updated == 0) {
                    Toast.makeText(getBaseContext(), "Failed Adding To Favourite :(", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Successfully Adding To Favourite :)", Toast.LENGTH_SHORT).show();
                }


            }
        });


      /*  trailer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:"
                                + movie.getTrailer()));
                startActivity(intent);

            }
        });

        trailer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:"
                                + "WtR9tqPa48s"));
                startActivity(intent);


            }
        });*/

    }


    public static Intent setIntent(Context context, Movie movie) {

        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(MOVIE, movie);


        return intent;
    }


}
