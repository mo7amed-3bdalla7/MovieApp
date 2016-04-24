package com.m7md.android.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by m7md on 3/25/16.
 */
public class GridAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Movie> items;

    GridAdapter(Context context, ArrayList<Movie> items) {
        mContext = context;
        this.items = items;


    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String baseUrl = "http://image.tmdb.org/t/p/w185/";

        final Movie movie = (Movie) getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        Picasso.with(mContext).load(baseUrl + movie.getPoster_path()).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.landscape) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("movie", movie);
                    mContext.startActivity(intent);


                } else {
                    Intent intent = DetailActivity.setIntent(mContext, movie);

                    mContext.startActivity(intent);
                }
            }
        });


        return convertView;
    }
}
