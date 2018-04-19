package com.mistdev.wooser.data.crud.asyncTasks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.models.IParser;

/**
 * Created by mcastro on 09/03/17.
 */

public class UpdateAsyncTask<T extends IParser<T>> extends AsyncTask<Long, Void, Integer> {

    private ContentResolver mContentResolver;
    private Uri mUri;
    private T mObj;

    protected UpdateAsyncTask(ContentResolver contentResolver, Uri uri, T obj) {
        mContentResolver = contentResolver;
        mUri = uri;
        mObj = obj;
    }

    @Override
    protected Integer doInBackground(Long... params) {

        long id = params[0];

        ContentValues cv = mObj.toContentValues();

        String selection = BaseColumns._ID + "=?";
        String[] args = { String.valueOf(id) };

        return mContentResolver.update(mUri, cv, selection, args);
    }
}