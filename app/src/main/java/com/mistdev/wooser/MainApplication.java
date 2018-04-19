package com.mistdev.wooser;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by mcastro on 13/04/17.
 */

public class MainApplication extends Application {

    private GoogleAnalytics mGoogleAnalytics;
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleAnalytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker() {

        if (mTracker == null) {
            mTracker = mGoogleAnalytics.newTracker(BuildConfig.googleAnalyticsTracker);
        }
        return mTracker;
    }

}
