package com.mistdev.wooser.main;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.data.models.User;

/**
 * Created by mcastro on 08/03/17.
 */

class MainView extends MvcView implements IMainView {

    private Listener mListener;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private TextView txtUserName;

    MainView(Context context, View root) {
        super(context, root);

        setupViews();
    }

    private void setupViews() {
        mToolbar = (Toolbar) getRootView().findViewById(R.id.toolbar);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public int getContainerViewIdRes() {
        return R.id.view_container;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_weight:
                mListener.onNavigationSelectedWeight();
                break;

            case R.id.nav_settings:
                mListener.onNavigationSelectedSettings();
                break;

            case R.id.nav_logout:
                mListener.onNavigationSelectedLogout();
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void configureActionBar(ActionBar actionBar) {
    }

    @Override
    public void setDrawer(Activity activity, User user) {

        mDrawer = (DrawerLayout) getRootView().findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);

        setNavigationView(user);

        toggle.syncState();
    }

    @Override
    public void setNavigation(NavigationView.OnNavigationItemSelectedListener listener) {

        NavigationView navigationView = (NavigationView) getRootView().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public DrawerLayout getDrawer() {
        return mDrawer;
    }

    private void setNavigationView(User user) {

        NavigationView navigationView = (NavigationView)mDrawer.findViewById(R.id.nav_view);

        try {
            navigationView.getMenu().getItem(0).setChecked(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        View headerView = navigationView.getHeaderView(0);

        txtUserName =  (TextView) headerView.findViewById(R.id.txt_user_name);
        txtUserName.setText(user.getName());
    }
}
