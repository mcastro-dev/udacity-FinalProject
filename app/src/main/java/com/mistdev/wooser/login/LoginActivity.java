package com.mistdev.wooser.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.mistdev.android_extensions.FileHelper;
import com.mistdev.google_drive.googleDriveBackup.GoogleDriveBackupFactory;
import com.mistdev.google_drive.googleDriveBackup.IGoogleDriveBackup;
import com.mistdev.google_drive.listeners.GoogleDriveConnectionListener;
import com.mistdev.google_drive.listeners.GoogleDriveFilesListener;
import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserAnalytics;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.WooserIntentsHandler;
import com.mistdev.wooser.data.database.WooserContract;
import com.mistdev.wooser.data.database.WooserDbHelper;
import com.mistdev.wooser.data.models.User;

import java.io.File;
import java.io.InputStream;

public class LoginActivity extends MvcControllerActivity implements
        WooserCredentialManager.WooserCredentialListener,
        ILoginView.Listener,
        LoginRecyclerAdapter.LoginAdapterListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BACKUP_REQUEST_CODE = 100;
    private static final int LOADER_USERS = 0;

    private ILoginView iView;
    private LoginRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_login);

        new WooserAnalytics(this);

        WooserCredentialManager.initialize(this);
        WooserCredentialManager.getInstance().initializeLoggedUser(this, this);
    }

    @Override
    public void setupView() {
        mRecyclerAdapter = new LoginRecyclerAdapter(null, this);
        iView = new LoginView(this, findViewById(android.R.id.content), mRecyclerAdapter);
        iView.setListener(this);
    }

    @Override
    public void gotLoggedUser(User user) {

        //User is logged, go to main
        if (user != null) {
            WooserIntentsHandler.openMainActivity(this);
            return;
        }

        getSupportLoaderManager().initLoader(LOADER_USERS, null, this).forceLoad();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BACKUP_REQUEST_CODE) {
            getSupportLoaderManager().restartLoader(LOADER_USERS, null, this).forceLoad();
        }
    }

    @Override
    public void onCreateUserClicked() {
        WooserIntentsHandler.openUserActivity(this);
    }

    @Override
    public void onUserClicked(User user) {

        WooserCredentialManager.getInstance().setLoggedUser(this, user);
        WooserIntentsHandler.openMainActivity(this);
    }

    @Override
    public void onBackupClicked() {
        WooserIntentsHandler.openBackup(this, BACKUP_REQUEST_CODE);
    }

    private void handleEmptyList() {

        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() <= 0) {
            iView.showEmptyListView();
            return;
        }

        iView.hideEmptyListView();
    }


    /* LOADER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, WooserContract.UserEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        changeAdapterCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        changeAdapterCursor(null);
    }

    private void changeAdapterCursor(Cursor cursor) {

        if(mRecyclerAdapter != null) {
            mRecyclerAdapter.changeCursor(cursor);
        }
        handleEmptyList();
    }
}
