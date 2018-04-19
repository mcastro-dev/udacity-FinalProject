package com.mistdev.mvc.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by mcastro on 04/02/17.
 */

public interface IOptionsMenu {
    void onCreateOptionsMenu(MenuInflater menuInflater, Menu menu);
    boolean onOptionsItemSelected(MenuItem item);
}
