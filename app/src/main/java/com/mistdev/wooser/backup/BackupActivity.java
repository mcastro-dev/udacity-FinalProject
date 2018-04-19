package com.mistdev.wooser.backup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mistdev.android_extensions.PermissionsHandler;
import com.mistdev.google_drive.googleDriveBackup.GoogleDriveBackupFactory;
import com.mistdev.google_drive.googleDriveBackup.IGoogleDriveBackup;
import com.mistdev.google_drive.listeners.ErrorCode;
import com.mistdev.google_drive.listeners.GoogleDriveConnectionListener;
import com.mistdev.google_drive.listeners.GoogleDriveFilesListener;
import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserAnalytics;
import com.mistdev.wooser.data.database.WooserDbHelper;
import com.mistdev.wooser.login.CopyFileAsync;

import java.io.File;
import java.io.InputStream;

public class BackupActivity extends MvcControllerActivity implements
        IBackupView.Listener,
        GoogleDriveConnectionListener,
        GoogleDriveFilesListener {

    private static final String MIME_TYPE_SQLITE_3 = "application/x-sqlite3";
    private static final String ANALYTICS_CATEGORY = "Google Drive";
    private static final int GET_ACCOUNTS_REQUEST_CODE = 1;

    private IBackupView iView;
    private WooserAnalytics mAnalytics;
    private IGoogleDriveBackup iGoogleDriveBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_backup);

        mAnalytics = new WooserAnalytics(this);

        if(hasAccountsPermission()) {
            initializeGoogleDrive();
        }
    }

    @Override
    public void setupView() {
        super.setupView();

        iView = new BackupView(this, findViewById(android.R.id.content), getSupportFragmentManager());
        iView.setListener(this);
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private boolean hasAccountsPermission() {

        PermissionsHandler permissionsHandler = new PermissionsHandler(this);

        //LOLLIPOP+ and doesn't have permission, ask for permission.
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP &&
                !permissionsHandler.hasPermission(Manifest.permission.GET_ACCOUNTS)) {

            permissionsHandler.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNTS_REQUEST_CODE);
            return false;
        }

        return true;
    }

    private void initializeGoogleDrive() {

        iGoogleDriveBackup = new GoogleDriveBackupFactory(this)
                .setConnectionListener(this)
                .setFilesListener(this)
                .build();

        if(!isGooglePlayServicesAvailable(this)) {
            return;
        }

        iView.showLoading();
        iGoogleDriveBackup.connect();
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);

        if(status != ConnectionResult.SUCCESS) {
            showError(getString(R.string.error_google_play_services));
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length <= 0) {
            return;
        }

        switch (requestCode) {

            case GET_ACCOUNTS_REQUEST_CODE:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    initializeGoogleDrive();

                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return iView.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iGoogleDriveBackup.onActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onStop() {
        if(iGoogleDriveBackup != null) {
            iGoogleDriveBackup.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onHomeSelected() {
        finish();
    }

    @Override
    public void upload() {

        mAnalytics.sendClicked(ANALYTICS_CATEGORY, "upload()");

        iView.showLoading();

        String filePath = WooserDbHelper.getDatabasePath(this);
        File file = new File(filePath);

        iGoogleDriveBackup.createFile(file, MIME_TYPE_SQLITE_3);
    }

    @Override
    public void download() {

        mAnalytics.sendClicked(ANALYTICS_CATEGORY, "download()");

        iView.showLoading();
        iGoogleDriveBackup.retrieveFile(WooserDbHelper.DB_NAME, MIME_TYPE_SQLITE_3);
    }

    @Override
    public void onGoogleDriveConnected() {

        mAnalytics.sendSuccess(ANALYTICS_CATEGORY, "Connected");
        iView.hideLoading();
    }

    @Override
    public void onFileExportedToGoogleDrive(boolean isSuccess) {

        iView.hideLoading();

        if(isSuccess) {
            showSuccess(getString(R.string.backup_upload_success_message));
            return;
        }

        showError(getString(R.string.backup_upload_failure_default_message));
    }

    @Override
    public void onFileImportedFromGoogleDrive(InputStream retrieved) {

        if(retrieved == null) {

            iView.hideLoading();
            showError(getString(R.string.backup_download_failure_default_message));
            return;
        }

        String filePath = WooserDbHelper.getDatabasePath(this);

        new CopyFileAsync(retrieved, filePath) {

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                iView.hideLoading();

                if(success) {
                    showSuccess(getString(R.string.backup_download_success_message));
                    return;
                }

                showError(getString(R.string.backup_download_failure_default_message));
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                iView.hideLoading();
            }
        }.execute();
    }

    @Override
    public void onFileError(@ErrorCode.Def String error) {

        iView.hideLoading();
        showError(error);
    }

    private void showError(String message) {

        mAnalytics.sendError(ANALYTICS_CATEGORY, message);
        iView.showFailure(message);
    }

    private void showSuccess(String message) {

        mAnalytics.sendSuccess(ANALYTICS_CATEGORY, message);
        iView.showSuccess(message);
    }
}
