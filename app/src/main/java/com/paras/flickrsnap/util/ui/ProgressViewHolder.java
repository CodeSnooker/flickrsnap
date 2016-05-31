package com.paras.flickrsnap.util.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.paras.flickrsnap.R;

/**
 * Copyright Text as per company
 */

/**
 * ProgressViewHolder.java - RecyclerView Holder class for displaying progress bar in the activity
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    /**
     * Generates instance of ProgressViewHolder
     *
     * @param v - View
     */
    public ProgressViewHolder(View v) {
        super(v);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
}