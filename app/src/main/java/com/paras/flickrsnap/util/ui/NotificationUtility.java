package com.paras.flickrsnap.util.ui;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Copyright Text as per company
 */

/**
 * NotificationUtility.java - Utility class to display general notifications
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class NotificationUtility {

    /**
     * Shows toast message and it makes sure that action done on UI thread
     *
     * @param activity
     * @param message
     */
    public static void showToast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast.makeText(activity, message, android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Shows Snackbar and it makes sure that action done on UI thread
     *
     * @param activity
     * @param parent
     * @param message
     * @param actionText
     */
    public static void showSnackbar(Activity activity, final View parent, final String message, final String actionText) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG)
                        .setAction(actionText, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });
                snackbar.show();
            }
        });

    }
}
