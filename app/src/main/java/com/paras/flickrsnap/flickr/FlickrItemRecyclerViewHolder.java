package com.paras.flickrsnap.flickr;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paras.flickrsnap.R;

/**
 * Copyright Text as per company
 */

/**
 * FlickrItemRecyclerViewHolder.java - View Holder for FlickrPhoto
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class FlickrItemRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public ImageView photo;
    public CardView cardView;
    public TextView tags;
    public ImageView favouriteImageView;

    /**
     * @param itemView
     *
     * @constructor
     */
    public FlickrItemRecyclerViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        cardView = (CardView) itemView.findViewById(R.id.card_view);

        title = (TextView) itemView.findViewById(R.id.country_name);
        photo = (ImageView) itemView.findViewById(R.id.country_photo);
        tags = (TextView) itemView.findViewById(R.id.tags);
        favouriteImageView = (ImageView) itemView.findViewById(R.id.favouriteImageView);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

    public void updateFavourite(boolean markFavourite) {
        if (markFavourite) {
            favouriteImageView.setImageResource(R.drawable.heart_red);
        } else {
            favouriteImageView.setImageResource(R.drawable.heart_outline);
        }
    }
}
