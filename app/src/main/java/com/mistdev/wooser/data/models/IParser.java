package com.mistdev.wooser.data.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by mcastro on 04/03/17.
 */

public interface IParser<T> {
    String idAlias();
    T fromCursor(Cursor cursor);
    ContentValues toContentValues();
}
