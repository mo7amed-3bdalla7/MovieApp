package com.m7md.android.mymovieapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
    private TextView movieTitle, movieDate, movieDeuration, movieRate, overView;
    private Movie movie;
    private ArrayList<Movie> movies;
    private ArrayList<Review> reviews;

    private Button favourite;
    private static final String MOVIE = "movie";
    private ListView reviewsList, trailerList;

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

        trailerList = (ListView) view.findViewById(R.id.trailer_list);
        reviewsList = (ListView) view.findViewById(R.id.Reviews_list);


        Intent intent = getActivity().getIntent();
        movie = (Movie) intent.getParcelableExtra(MOVIE);

        if (movie != null) {

            Picasso.with(getActivity()).load(baseUrl + movie.getPoster_path()).into(moviePoster);
            movieTitle.setText(movie.getTitle());
            movieDate.setText(movie.formatDate());
            movieDeuration.setText(movie.getMinutes() + " Min");
            movieRate.setText(movie.getVote_average() + "/10");
            overView.setText(movie.getOverview());

            ItemsTask trailerTask = new ItemsTask();
            trailerTask.setContext(getActivity());
            trailerTask.execute(movie.getID() + "", "trailer");

            ItemsTask reviewsTask = new ItemsTask();
            reviewsTask.setContext(getActivity());
            reviewsTask.execute(movie.getID() + "", "review");
            try {
                movies = trailerTask.get();
                reviews = reviewsTask.get();
                movie.setTrailer(movies.get(0).getTrailer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            TrailerAdapter trailerListAdapter = new TrailerAdapter(getActivity(), movies);
            ReviewsAdapter reviewsListAdapter = new ReviewsAdapter(getActivity(), reviews);


            trailerList.setAdapter(trailerListAdapter);
            reviewsList.setAdapter(reviewsListAdapter);

            setListViewHeightBasedOnItems(trailerList);
            setListViewHeightBasedOnItems(reviewsList);

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

    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
