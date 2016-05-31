package com.paras.flickrsnap.flickr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright Text as per company
 */

/**
 * FlickrAPI.java - An interface provides access to Flickr APIs entities
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public interface FlickrAPI {

    // Get photo Information
    @GET("?method=flickr.photos.getInfo")
    Call<Object> getInfo(@Query("photo_id") String photoID);

    // Get list by tags
    @GET("?method=flickr.photos.search&media=photo&extras=tags")
    Call<FlickrResponse> findByTag(@Query("tags") String tags, @Query("per_page") int limit, @Query("page") int page);
}
