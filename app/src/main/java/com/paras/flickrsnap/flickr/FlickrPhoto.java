package com.paras.flickrsnap.flickr;

import android.util.Log;

import com.google.gson.annotations.SerializedName;


/**
 * Copyright Text as per company
 */

/**
 * FlickrPhoto.java - POJO class for Photo received in Flickr API Response's Photo Collection
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class FlickrPhoto {

    private String id;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private String tags;
    private String thumbnailURL;

    private transient boolean exploded = false;
    private boolean favourite = false;

    @SerializedName("ispublic")
    private int bPublic;

    @SerializedName("isfriend")
    private int bFriend;

    @SerializedName("isfamily")
    private int family;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getbPublic() {
        return bPublic;
    }

    public void setbPublic(int bPublic) {
        this.bPublic = bPublic;
    }

    public int getbFriend() {
        return bFriend;
    }

    public void setbFriend(int bFriend) {
        this.bFriend = bFriend;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public String getThumbnailURL() {
        if (thumbnailURL == null) {
            thumbnailURL = "https://farm" + this.getFarm() + ".staticflickr.com/" + this.getServer() + "/" + this.getId() + "_" + this.getSecret();
        }
        return thumbnailURL;
    }

    public String getTags() {
        if (!exploded) {

            tags = tags == null ? "" : tags;
            String[] tempTags = tags.split(" ");
            tags = "";
            for (int i = 0; i < tempTags.length; i++) {
                tags += " #" + tempTags[i];
            }
            tags = tags.trim();
            exploded = true;
        }
        return tags;
    }

    public void setTags(String tags) {
        Log.d("TAGS", tags);
        this.tags = tags;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
