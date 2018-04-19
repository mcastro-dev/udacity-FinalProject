package com.mistdev.wooser;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by mcastro on 13/04/17.
 */

public class WooserAnalytics {

    private static final String ACTION_SUCCESS = "Success";
    private static final String ACTION_ERROR = "Error";
    private static final String ACTION_CLICKED = "Clicked";
    private static final String ACTION_CREATED = "Created";
    private static final String ACTION_UPDATED = "Updated";

    private Tracker mTracker;

    public WooserAnalytics(Activity activity) {

        MainApplication application = (MainApplication) activity.getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(activity.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void send(String category, String action, String label, long value) {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    private void send(String category, String action, String label) {

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public void sendSuccess(String category, String label) {
        send(category, ACTION_SUCCESS, label);
    }

    public void sendError(String category, String label) {
        send(category, ACTION_ERROR, label);
    }

    public void sendClicked(String category, String label) {
        send(category, ACTION_CLICKED, label);
    }

    public void sendCreated(String category, String label, long value) {
        send(category, ACTION_CREATED, label, value);
    }

    public void sendUpdate(String category, String label, long value) {
        send(category, ACTION_UPDATED, label, value);
    }
}
