package com.mistdev.google_drive.googleDriveBackup;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.mistdev.google_drive.listeners.ErrorCode;
import com.mistdev.google_drive.listeners.GoogleDriveConnectionListener;
import com.mistdev.google_drive.listeners.GoogleDriveFilesListener;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mcastro on 08/04/17.
 */

class GoogleDriveBackup implements
        IGoogleDriveBackup,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = GoogleDriveBackup.class.getSimpleName();

    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private GoogleDriveFilesListener mFilesListener;
    private GoogleDriveConnectionListener mConnectionListener;
    private int mResolutionRequestCode = 6006;

    GoogleDriveBackup(Activity activity) {

        mActivity = activity;
        setGoogleDriveClient();

    }

    private void setGoogleDriveClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private boolean isConnected() {

        if(!mGoogleApiClient.isConnected()) {
            if(mFilesListener != null) {
                mFilesListener.onFileError(ErrorCode.NOT_CONNECTED);
            }
            return false;
        }

        return true;
    }

    @Override
    public void setFilesListener(GoogleDriveFilesListener listener) {
        mFilesListener = listener;
    }

    @Override
    public void setConnectionListener(GoogleDriveConnectionListener listener) {
        mConnectionListener = listener;
    }

    @Override
    public void setResolutionRequestCode(int requestCode) {
        mResolutionRequestCode = requestCode;
    }

    @Override
    synchronized public void connect() {

        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    synchronized public void disconnect() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void createFile(final File file, final String mimeType) {

        if(!isConnected()) {
            return;
        }

        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult result) {

                new WriteToDriveContentsAsync(result, file) {

                    @Override
                    protected void onPostExecute(DriveContents driveContents) {
                        super.onPostExecute(driveContents);

                        createFile(driveContents, file.getName(), mimeType);
                    }
                }.execute();
            }
        });
    }

    private void createFile(final DriveContents driveContents, final String filename, final String mimeType) {

        if (driveContents == null) {
            if(mFilesListener != null) {
                mFilesListener.onFileError(ErrorCode.CREATING_FILE);
            }
            return;
        }

        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(filename)
                .setMimeType(mimeType)
                .build();

        Drive.DriveApi.getAppFolder(mGoogleApiClient)
                .createFile(mGoogleApiClient, changeSet, driveContents)
                .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {

                    @Override
                    public void onResult(@NonNull DriveFolder.DriveFileResult result) {

                        mFilesListener.onFileExportedToGoogleDrive(result.getStatus().isSuccess());
                    }
                });
    }

    @Override
    public void retrieveFile(final String filename, final String mimeType) {

        if(!isConnected()) {
            return;
        }

        DriveFolder appFolder = Drive.DriveApi.getAppFolder(mGoogleApiClient);

        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, mimeType))
                .addFilter(Filters.contains(SearchableField.TITLE, filename))
                .build();

        appFolder.queryChildren(mGoogleApiClient, query)
                .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                    @Override
                    public void onResult(@NonNull DriveApi.MetadataBufferResult result) {

                        if (!result.getStatus().isSuccess()) {
                            if(mFilesListener != null) {
                                mFilesListener.onFileError(ErrorCode.RETRIEVING_FILE);
                            }
                            return;
                        }

                        DriveId driveId = null;
                        long currentMetadataCreatedDate = 0;

                        for(Metadata metadata : result.getMetadataBuffer()) {

                            if(!metadata.isDataValid()) {
                                continue;
                            }

                            long metadataCreatedDate = metadata.getCreatedDate().getTime();

                            if(metadataCreatedDate > currentMetadataCreatedDate) {
                                currentMetadataCreatedDate = metadataCreatedDate;
                                driveId = metadata.getDriveId();
                            }
                        }

                        retrieveFile(driveId);
                    }
                });
    }

    private void retrieveFile(DriveId fileDriveId) {

        if(fileDriveId == null) {
            if(mFilesListener != null) {
                mFilesListener.onFileError(ErrorCode.RETRIEVING_FILE);
            }
            return;
        }

        final DriveFile driveFile = fileDriveId.asDriveFile();

        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(@NonNull DriveApi.DriveContentsResult result) {

                        if (!result.getStatus().isSuccess()) {
                            if(mFilesListener != null) {
                                mFilesListener.onFileError(ErrorCode.RETRIEVING_FILE);
                            }
                            return;
                        }

                        if(mFilesListener != null) {
                            mFilesListener.onFileImportedFromGoogleDrive(result.getDriveContents().getInputStream());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

        if(requestCode != mResolutionRequestCode) {
            return;
        }

        if (resultCode == RESULT_OK) {

            mGoogleApiClient.connect();
            return;
        }

        //Error
        if(mFilesListener != null) {
            mFilesListener.onFileError(ErrorCode.NOT_CONNECTED);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(mConnectionListener != null) {
            mConnectionListener.onGoogleDriveConnected();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(mActivity, result.getErrorCode(), 0).show();
            return;
        }

        try {
            result.startResolutionForResult(mActivity, mResolutionRequestCode);

        } catch (IntentSender.SendIntentException e) {
            Log.e(LOG_TAG, "### onConnectionFailed::Exception -> while starting resolution activity", e);
        }
    }
}
