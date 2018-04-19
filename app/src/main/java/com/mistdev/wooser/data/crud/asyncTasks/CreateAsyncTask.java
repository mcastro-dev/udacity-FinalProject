package com.mistdev.wooser.data.crud.asyncTasks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;

import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.models.IParser;

/**
 * Created by mcastro on 08/03/17.
 */

public class CreateAsyncTask<T extends IParser<T>> extends AsyncTask<Void, Void, Long> {

    private ContentResolver mContentResolver;
    private Uri mUri;
    private T mObj;

    protected CreateAsyncTask(ContentResolver contentResolver, Uri uri, T obj) {
        mContentResolver = contentResolver;
        mUri = uri;
        mObj = obj;
    }

    @Override
    protected Long doInBackground(Void... params) {

        long id = CRUD.INVALID_ID;

        ContentValues cv = mObj.toContentValues();
        Uri resultUri = mContentResolver.insert(mUri, cv);

        if(resultUri != null) {
            id = ContentUris.parseId(resultUri);
        }

        return id;
    }
}