package com.mistdev.wooser.createEntry;

import android.support.v7.app.ActionBar;

import com.mistdev.mvc.view.IOptionsMenu;
import com.mistdev.wooser.data.models.WeightEntry;

/**
 * Created by mcastro on 10/03/17.
 */

interface ICreateEntryView extends IOptionsMenu {

    interface Listener {
        void onCreateEntryClicked(WeightEntry entry);
        void onHomeSelected();
    }

    void setListener(Listener listener);
    void showError(String message);
    void setupActionBar(ActionBar actionBar);
}
