package com.m7md.android.mymovieapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by m7md on 4/23/16.
 */
public class DetailFragment extends Fragment {
    TextView movieTitle, movieDate, movieDeuration, movieRate, overView;
    Movie movie;
    ListView trailerList;
    ArrayList<Movie> movies;

    Button favourite;
    private static final String MOVIE = "movie";
    private OnItemSelectedListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_detail,
                container, false);


        String baseUrl = "http://image.tmdb.org/t/p/w185/";

        ImageView moviePoster = (ImageView) view.findViewById(R.id.movie_poster);

        movieTitle = (TextView) view.findViewById(R.id.movie_title);
        movieDate = (TextView) view.findViewById(R.id.movie_date);
        movieDeuration = (TextView) view.findViewById(R.id.movie_minuts);
        movieRate = (TextView) view.findViewById(R.id.movie_rate);
        overView = (TextView) view.findViewById(R.id.overview);
        favourite = (Button) view.findViewById(R.id.fav_btn);
      /*  trailer1 = (LinearLayout) findViewById(R.id.trailer1);
        trailer2 = (LinearLayout) findViewById(R.id.trailer2);*/
        trailerList = (ListView) view.findViewById(R.id.trailer_list);


        Intent intent = getActivity().getIntent();
        movie = (Movie) intent.getSerializableExtra(MOVIE);

        if (movie != null) {

            Picasso.with(getActivity()).load(baseUrl + movie.getPoster_path()).into(moviePoster);
            movieTitle.setText(movie.getTitle());
            movieDate.setText(movie.formatDate());
            movieDeuration.setText(movie.getMinutes() + " Min");
            movieRate.setText(movie.getVote_average() + "/10");
            overView.setText(movie.getOverview());

            MainActivity.itemsTask itemsTask = new MainActivity.itemsTask();
            itemsTask.setContext(getActivity());
            itemsTask.execute(movie.getID() + "", "trailer");
            try {
                movies = itemsTask.get();
                movie.setTrailer(movies.get(0).getTrailer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            trailerAdapter listAdapter = new trailerAdapter(getActivity(), movies);


            trailerList.setAdapter(listAdapter);

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDB movieDB = new MovieDB(getActivity());

                    int updated = movieDB.updateFavourite(movie.getID());
                    if (updated == 0) {
                        Toast.makeText(getActivity(), "Failed Adding To Favourite :(", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Successfully Adding To Favourite :)", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }


        return view;
    }

    public void displayFrame() {

        View view = getView().findViewById(R.id.detail_Frame);
        view.setVisibility(View.INVISIBLE);
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }


    public void updateDetail(String uri) {

        listener.onRssItemSelected("");
    }
}
