package com.paras.flickrsnap.flickr;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Copyright Text as per company
 */

/**
 * FlickrPhotoCollection.java - POJO class for Photo Collection received in Flickr API Response
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class FlickrPhotoCollection {

    private int page;
    private int pages;

    @SerializedName("perpage")
    private int perPage;
    private int total;

    @SerializedName("photo")
    private ArrayList<FlickrPhoto> photos;
    private String tags;

    public int getPage() {
        return page;
    }

    public FlickrPhotoCollection setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public FlickrPhotoCollection setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public int getPerPage() {
        return perPage;
    }

    public FlickrPhotoCollection setPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public FlickrPhotoCollection setTotal(int total) {
        this.total = total;
        return this;
    }

    public ArrayList<FlickrPhoto> getPhotos() {
        return photos;
    }

    public FlickrPhotoCollection setPhotos(ArrayList<FlickrPhoto> photos) {
        this.photos = photos;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public FlickrPhotoCollection setTags(String tags) {
        this.tags = tags;
        return this;
    }
}
