package com.mistdev.google_drive.googleDriveBackup;

import com.mistdev.google_drive.listeners.GoogleDriveConnectionListener;
import com.mistdev.google_drive.listeners.GoogleDriveFilesListener;

import java.io.File;

/**
 * Created by mcastro on 08/04/17.
 */

public interface IGoogleDriveBackup {

    void setFilesListener(GoogleDriveFilesListener listener);
    void setConnectionListener(GoogleDriveConnectionListener listener);
    void setResolutionRequestCode(int requestCode);
    void connect();
    void disconnect();
    void createFile(final File file, final String mimeType);
    void retrieveFile(final String filename, final String mimeType);
    void onActivityResult(int requestCode, int resultCode);
}
