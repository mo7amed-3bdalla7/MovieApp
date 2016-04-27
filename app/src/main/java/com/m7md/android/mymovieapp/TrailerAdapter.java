package com.m7md.android.mymovieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by m7md on 4/23/16.
 */
public class TrailerAdapter extends BaseAdapter {
    private final Context mContext;
    ArrayList<Movie> movies;

    TrailerAdapter(Context context, ArrayList<Movie> items) {
        mContext = context;
        this.movies = items;


    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Movie movie = (Movie) getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        }
        LinearLayout trailer = (LinearLayout) convertView.findViewById(R.id.trailer);
        TextView trailerTxt = (TextView) convertView.findViewById(R.id.trailer_txt);

        trailerTxt.setText("Trailer " + (position + 1));

        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube:"
                                    + movie.getTrailer()));
                    mContext.startActivity(intent);

                } catch (Exception e) {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v="
                                    + movie.getTrailer()));
                    mContext.startActivity(intent);

                }

            }
        });


        return convertView;
    }

}
