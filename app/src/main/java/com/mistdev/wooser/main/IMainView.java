package com.mistdev.wooser.main;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mistdev.wooser.data.models.User;

/**
 * Created by mcastro on 08/03/17.
 */

interface IMainView {

    interface Listener {
        void onNavigationSelectedWeight();
        void onNavigationSelectedSettings();
        void onNavigationSelectedLogout();
    }

    void setListener(Listener listener);

    boolean onNavigationItemSelected(@NonNull MenuItem item);

    void setDrawer(Activity activity, User user);
    void setNavigation(NavigationView.OnNavigationItemSelectedListener listener);
    @IdRes int getContainerViewIdRes();

    Toolbar getToolbar();
    DrawerLayout getDrawer();

}
