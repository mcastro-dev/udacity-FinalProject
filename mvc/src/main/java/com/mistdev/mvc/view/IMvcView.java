package com.mistdev.mvc.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.view.View;

/**
 * Created by mcastro on 02/02/17.
 */

interface IMvcView {
    /**
     * Get the root Android View which is used internally by the MVC View for presenting data
     * to the user.
     * @return Root Android View of this MVC View
     */
    View getRootView();

    /**
     * Gets the context of this view.
     * @return Context
     */
    Context getContext();

    /**
     * Configure the action bar back button, title, etc
     * @param actionBar the actionBar to be handled
     */
    void configureActionBar(ActionBar actionBar);

    /**
     * Get string from resources
     * @param resId the string resource id
     * @return the desired string
     */
    String getString(@StringRes int resId);
}
