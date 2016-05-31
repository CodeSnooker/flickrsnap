package com.paras.flickrsnap.flickr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.paras.flickrsnap.Constants;
import com.paras.flickrsnap.R;
import com.paras.flickrsnap.util.ui.ProgressViewHolder;

import java.util.List;
import java.util.Random;

/**
 * Copyright Text as per company
 */

/**
 * FlickrRecyclerViewAdapter.java - RecyclerView Adapter for Flickr Photos
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FlickrPhoto> photos;
    private String requestedTag = "";
    private SharedPreferences preferences;

    protected Activity activity;
    private boolean onLastPage = false;

    // Two view types which will be used to determine whether a row should be displaying
    // data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_PHOTO_ITEM = 1;


    /**
     * @param activity
     * @param photoList
     */
    public FlickrRecyclerViewAdapter(Activity activity, List<FlickrPhoto> photoList) {
        this.activity = activity;
        this.photos = photoList;
        preferences = activity.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

    }

    /**
     * @param parent
     * @param viewType
     *
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_PHOTO_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flickr_list_card, null);
            viewHolder = new FlickrItemRecyclerViewHolder(layoutView);
        } else {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            viewHolder = new ProgressViewHolder(layoutView);
        }

        return viewHolder;
    }

    /**
     * Generates spannable text to highlight the request tag in the list of given tags
     *
     * @param requestedTag - Tag requested by end user
     * @param photoTags    - tags associated to FlickrPhoto
     *
     * @return Spannable - Spannable Text
     */
    private Spannable getSpannableTags(String requestedTag, String photoTags) {

        photoTags = photoTags + " ";
        requestedTag = requestedTag + " ";
        Spannable spannable = new SpannableString(photoTags);
        int startIndex = photoTags.indexOf(requestedTag);
        int endIndex = startIndex + requestedTag.length();
        int selectedTagColor = activity.getResources().getColor(R.color.colorSelectedTag);

        // Just make sure search is within boundary
        startIndex = Math.max(0, startIndex);
        endIndex = Math.min(endIndex, photoTags.length());

        spannable.setSpan(new ForegroundColorSpan(selectedTagColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    /**
     * @param position
     *
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position < photos.size() ? VIEW_TYPE_PHOTO_ITEM : VIEW_TYPE_LOADING;
    }

    /**
     * Mark item as Favourite/UnFavourite and updates view and SharedPreferences
     *
     * @param position
     *
     * @return
     */
    private boolean addOrRemoveToFavourities(int position) {
        Log.d("View Adapter", "Position in Array: " + position);
        FlickrPhoto flickrPhoto = photos.get(position);
        boolean favouriteStatus = false;
        if (flickrPhoto != null) {
            favouriteStatus = preferences.getBoolean(flickrPhoto.getId(), false);
            favouriteStatus = !favouriteStatus;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(flickrPhoto.getId(), favouriteStatus);
            editor.commit();
            flickrPhoto.setFavourite(favouriteStatus);
        }

        return favouriteStatus;

    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FlickrItemRecyclerViewHolder) {
            final FlickrItemRecyclerViewHolder viewHolder = (FlickrItemRecyclerViewHolder) holder;
            viewHolder.title.setText(photos.get(position).getTitle());


            viewHolder.favouriteImageView.setTag(photos.get(position).getId());
            viewHolder.favouriteImageView.setTag(new Integer(position + ""));

            viewHolder.favouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isFavourite = addOrRemoveToFavourities(new Integer(v.getTag().toString()).intValue());
                    viewHolder.updateFavourite(isFavourite);
                }
            });

            viewHolder.updateFavourite(photos.get(position).isFavourite());

            viewHolder.tags.setText(this.getSpannableTags(requestedTag, photos.get(position).getTags()));

            Random rnd = new Random();
            viewHolder.photo.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

            ImageOptions options = new ImageOptions();

            options.memCache = true;
            options.fileCache = true;
            options.animation = AQuery.FADE_IN;

            AQuery aQuery = new AQuery(viewHolder.photo);
            String thumbnailURL = photos.get(position).getThumbnailURL() + ".jpg";
            aQuery.id(R.id.country_photo).image(thumbnailURL, options);
        } else {

            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {

        int itemCounts = this.photos.size();
        return (itemCounts > 0) ? (isOnLastPage() ? itemCounts : ++itemCounts) : itemCounts;
    }

    /**
     * @param photos
     *
     * @return
     */
    public FlickrRecyclerViewAdapter setPhotos(List<FlickrPhoto> photos) {
        this.photos = photos;
        return this;
    }

    /**
     * @param photos
     *
     * @return
     */
    public FlickrRecyclerViewAdapter appendPhotos(List<FlickrPhoto> photos) {
        this.photos.addAll(photos);
        return this;
    }

    /**
     * @param requestedTag
     *
     * @return
     */
    public FlickrRecyclerViewAdapter setRequestedTag(String requestedTag) {
        this.requestedTag = requestedTag;
        return this;
    }

    /**
     * @return
     */
    public boolean isOnLastPage() {
        return onLastPage;
    }

    /**
     * @param onLastPage
     *
     * @return
     */
    public FlickrRecyclerViewAdapter setOnLastPage(boolean onLastPage) {
        this.onLastPage = onLastPage;
        return this;
    }
}
