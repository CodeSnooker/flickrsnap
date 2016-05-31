package com.paras.flickrsnap.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Copyright Text as per company
 */

/**
 * Utility class of Network Connection
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class NetworkConnection {

    /**
     * Checks if the internet connection is reachable
     *
     * @param ctx - Context of Application/Activity
     *
     * @return boolean TRUE - if connection is reachable; FALSE - if connection is not reachable
     */
    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
