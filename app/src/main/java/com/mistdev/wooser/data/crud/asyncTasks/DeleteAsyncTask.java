package com.mistdev.wooser.data.crud.asyncTasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.mistdev.wooser.data.models.IParser;

/**
 * Created by mcastro on 09/03/17.
 */

public class DeleteAsyncTask<T extends IParser<T>> extends AsyncTask<Long, Void, Integer> {

    private ContentResolver mContentResolver;
    private Uri mUri;

    protected DeleteAsyncTask(ContentResolver contentResolver, Uri uri) {
        mContentResolver = contentResolver;
        mUri = uri;
    }

    @Override
    protected Integer doInBackground(Long... params) {

        long id = params[0];

        String selection = BaseColumns._ID + "=?";
        String[] args = { String.valueOf(id) };

        return mContentResolver.delete(mUri, selection, args);
    }
}
