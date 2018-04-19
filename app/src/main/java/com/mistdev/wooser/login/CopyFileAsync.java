package com.mistdev.wooser.login;

import android.os.AsyncTask;

import com.mistdev.android_extensions.FileHelper;

import java.io.InputStream;

/**
 * Created by mcastro on 10/04/17.
 */

public class CopyFileAsync extends AsyncTask<Void, Integer, Boolean> {

    private InputStream mInputStream;
    private String mOutputPath;

    public CopyFileAsync(InputStream inputStream, String outputPath) {
        mInputStream = inputStream;
        mOutputPath = outputPath;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return FileHelper.copyFile(mInputStream, mOutputPath);
    }
}
