package com.mistdev.wooser.data.crud.asyncTasks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import com.mistdev.wooser.data.models.IParser;

/**
 * Created by mcastro on 09/03/17.
 */


public class ReadAsyncTask<T extends IParser<T>> extends AsyncTask<Long, Void, T> {

    private ContentResolver mContentResolver;
    private Uri mUri;
    private Class<T> tClass;

    protected ReadAsyncTask(ContentResolver contentResolver, Uri uri, Class<T> tClass) {
        mContentResolver = contentResolver;
        mUri = uri;
        this.tClass = tClass;
    }

    @Override
    protected T doInBackground(Long... params) {

        T nObj = null;

        Cursor cursor = null;

        try {

            long id = params[0];

            IParser<T> parser;

            try {
                parser = tClass.newInstance();

            } catch (Exception e) {
                throw new RuntimeException("IParser newInstance() exception");
            }

            String selection = parser.idAlias() + "=?";
            String[] args = { String.valueOf(id) };

            cursor = mContentResolver.query(mUri, null, selection, args, null);
            if(cursor != null && cursor.moveToFirst()) {

                nObj = parser.fromCursor(cursor);
            }

        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }

        return nObj;
    }
}