package com.paras.flickrsnap.flickr;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright Text as per company
 */

/**
 * FlickrResponse.java - POJO class for Response received from Flickr API
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class FlickrResponse {


    @SerializedName("photos")
    private FlickrPhotoCollection flickrPhotoCollection;

    @SerializedName("stat")
    private String status;

    private int code;
    private String message;

    public FlickrPhotoCollection getFlickrPhotoCollection() {
        return flickrPhotoCollection;
    }

    public FlickrResponse setFlickrPhotoCollection(FlickrPhotoCollection flickrPhotoCollection) {
        this.flickrPhotoCollection = flickrPhotoCollection;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public FlickrResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getCode() {
        return code;
    }

    public FlickrResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public FlickrResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isFailed() {
        return (this.status != null) ? (this.status.equalsIgnoreCase("fail")) : true;
    }
}
