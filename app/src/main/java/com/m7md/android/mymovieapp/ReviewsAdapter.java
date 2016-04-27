package com.m7md.android.mymovieapp;

import android.content.Context;
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
public class ReviewsAdapter extends BaseAdapter {
    private final Context mContext;
    ArrayList<Review> reviews;

    ReviewsAdapter(Context context, ArrayList<Review> items) {
        mContext = context;
        this.reviews = items;


    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Review review = (Review) getItem(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.reviw_item, parent, false);
        }
        LinearLayout trailer = (LinearLayout) convertView.findViewById(R.id.trailer);
        TextView reviewAuthor = (TextView) convertView.findViewById(R.id.Review_author);
        TextView reviewContent = (TextView) convertView.findViewById(R.id.Review_content);

        reviewAuthor.setText("Author :  " + review.getAuthor());
        reviewContent.setText("Content : \n" + review.getContent()+"\n");

        return convertView;
    }

}
