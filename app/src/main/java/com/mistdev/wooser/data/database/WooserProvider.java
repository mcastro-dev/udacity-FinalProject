package com.mistdev.wooser.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mistdev.wooser.data.database.WooserContract.*;

/**
 * Created by mcastro on 04/03/17.
 */

public class WooserProvider extends ContentProvider {

    private WooserDbHelper dbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int USER = 100;
    static final int WEIGHT_ENTRY = 200;
    static final int PLAN = 300;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WooserContract.CONTENT_AUTHORITY;

        // Corresponding code for each type of URI to add.
        matcher.addURI(authority, WooserContract.PATH_USER, USER);
        matcher.addURI(authority, WooserContract.PATH_WEIGHT_ENTRY, WEIGHT_ENTRY);
        matcher.addURI(authority, WooserContract.PATH_PLAN, PLAN);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WooserDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        //implement this if needed
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {

            case USER:
                WooserQueryBuilder queryBuilder = new WooserQueryBuilder();
                cursor = queryBuilder.queryUser(db, selection, selectionArgs, sortOrder);//db.query(userTable, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case WEIGHT_ENTRY:
                cursor = db.query(WeightEntryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PLAN:
                cursor = db.query(PlanEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        Context context = getContext();
        if(context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id;

        switch (sUriMatcher.match(uri)) {

            case USER:
                id = db.insert(UserEntry.TABLE_NAME, null, values);
                returnUri = UserEntry.buildUriWithId(id);
                break;

            case WEIGHT_ENTRY:
                id = db.insert(WeightEntryEntry.TABLE_NAME, null, values);
                returnUri = WeightEntryEntry.buildUriWithId(id);
                break;

            case PLAN:
                id = db.insert(PlanEntry.TABLE_NAME, null, values);
                returnUri = PlanEntry.buildUriWithId(id);
                break;

            default:
                throw new UnsupportedOperationException();

        }

        Context context = getContext();
        if(context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleteCount = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case USER:
                deleteCount = db.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case WEIGHT_ENTRY:
                deleteCount = db.delete(WeightEntryEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PLAN:
                deleteCount = db.delete(PlanEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException();

        }

        Context context = getContext();
        if(context != null && deleteCount != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case USER:
                updateCount = db.update(UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case WEIGHT_ENTRY:
                updateCount = db.update(WeightEntryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case PLAN:
                updateCount = db.update(PlanEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException();

        }

        Context context = getContext();
        if(context != null && updateCount != 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return updateCount;
    }
}
