package com.mistdev.wooser.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserAnalytics;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.WooserIntentsHandler;
import com.mistdev.wooser.settings.SettingsFragment;
import com.mistdev.wooser.settings.SettingsListener;
import com.mistdev.wooser.weight.WeightFragment;

public class MainActivity extends MvcControllerActivity implements
        IMainView.Listener,
        SettingsListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_WEIGHT = "TAG_WEIGHT";
    private static final String TAG_SETTINGS = "TAG_SETTINGS";

    private IMainView iView;
    private WooserCredentialManager mCredentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCredentialManager = WooserCredentialManager.getInstance();
        onCreate(R.layout.activity_main);

        new WooserAnalytics(this);

        if(savedInstanceState == null) {
            onNavigationSelectedWeight();//Default
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setupView() {
        iView = new MainView(this, findViewById(android.R.id.content));
        setSupportActionBar(iView.getToolbar());

        iView.setListener(this);
        iView.setDrawer(this, mCredentialManager.getLoggedUser());
        iView.setNavigation(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = iView.getDrawer();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onNavigationSelectedWeight() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        WeightFragment weightFragment = (WeightFragment)fragmentManager.findFragmentByTag(TAG_WEIGHT);

        if(weightFragment == null) {
            weightFragment = new WeightFragment();
        }

        fragmentManager.beginTransaction()
                .replace(iView.getContainerViewIdRes(), weightFragment, TAG_WEIGHT)
                .commit();
    }

    @Override
    public void onNavigationSelectedSettings() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        SettingsFragment settingsFragment = (SettingsFragment)fragmentManager.findFragmentByTag(TAG_SETTINGS);

        if(settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }

        fragmentManager.beginTransaction()
                .replace(iView.getContainerViewIdRes(), settingsFragment, TAG_SETTINGS)
                .commit();
    }

    @Override
    public void onNavigationSelectedLogout() {
        logoutUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return iView.onNavigationItemSelected(item);
    }

    @Override
    public void onLoggedUserDeleted() {
        logoutUser();
    }

    private void logoutUser() {

        mCredentialManager.logoutUser(this);
        WooserIntentsHandler.openLoginActivity(this);
    }
}
