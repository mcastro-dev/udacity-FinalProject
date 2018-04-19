package com.mistdev.google_drive.googleDriveBackup;

import android.os.AsyncTask;

import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mcastro on 10/04/17.
 */

class WriteToDriveContentsAsync extends AsyncTask<Void, Void, DriveContents> {

    private DriveApi.DriveContentsResult mDriveContentsResult;
    private File mFile;

    WriteToDriveContentsAsync(DriveApi.DriveContentsResult result, File file) {
        mDriveContentsResult = result;
        mFile = file;
    }

    @Override
    protected DriveContents doInBackground(Void... params) {

        final DriveContents driveContents = mDriveContentsResult.getDriveContents();

        OutputStream output = null;
        InputStream input = null;

        try {

            output = driveContents.getOutputStream();
            input = new FileInputStream(mFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try {
                if(output != null) {
                    output.flush();
                    output.close();
                }

                if(input != null) {
                    input.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return driveContents;
    }
}
