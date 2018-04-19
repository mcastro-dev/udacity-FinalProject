package com.mistdev.google_drive.googleDriveBackup;

import android.app.Activity;

import com.mistdev.google_drive.listeners.GoogleDriveConnectionListener;
import com.mistdev.google_drive.listeners.GoogleDriveFilesListener;

/**
 * Created by mcastro on 10/04/17.
 */

public class GoogleDriveBackupFactory {

    private GoogleDriveBackup mGoogleDriveBackup;

    public GoogleDriveBackupFactory(Activity activity) {
        mGoogleDriveBackup = new GoogleDriveBackup(activity);
    }

    public GoogleDriveBackupFactory setFilesListener(GoogleDriveFilesListener listener) {
        mGoogleDriveBackup.setFilesListener(listener);
        return this;
    }

    public GoogleDriveBackupFactory setConnectionListener(GoogleDriveConnectionListener listener) {
        mGoogleDriveBackup.setConnectionListener(listener);
        return this;
    }

    public GoogleDriveBackupFactory setResolutionRequestCode(int requestCode) {
        mGoogleDriveBackup.setResolutionRequestCode(requestCode);
        return this;
    }

    public IGoogleDriveBackup build() {
        return mGoogleDriveBackup;
    }
}
