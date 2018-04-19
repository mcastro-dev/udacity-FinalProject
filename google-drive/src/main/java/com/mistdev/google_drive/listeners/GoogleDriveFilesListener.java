package com.mistdev.google_drive.listeners;

import java.io.InputStream;

/**
 * Created by mcastro on 08/04/17.
 */

public interface GoogleDriveFilesListener {

    void onFileExportedToGoogleDrive(boolean isSuccess);
    void onFileImportedFromGoogleDrive(InputStream retrieved);
    void onFileError(@ErrorCode.Def String error);
}
