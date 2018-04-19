package com.mistdev.wooser.data.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import com.mistdev.wooser.data.database.WooserContract.*;
import com.mistdev.wooser.data.models.WeightEntry;

/**
 * Created by mcastro on 23/03/17.
 */

class WooserQueryBuilder extends SQLiteQueryBuilder {


    private Cursor query(SQLiteDatabase db, String tables, String[] projection,
                         String selection, String[] selectionArgs, String sortOrder, String limit) {

        this.setTables(tables);
        return super.query(db, projection, selection, selectionArgs, null, null, sortOrder, limit);
    }


    /* USER
     * --------------------------------------------------------------------------------------------------------------*/
    Cursor queryUser(SQLiteDatabase db, String selection, String[] selectionArgs, String sortOrder) {

        String weightEntryTableAlias = "w";

        String userTable = UserEntry.TABLE_NAME + " LEFT JOIN " +
                "(SELECT * FROM " + WeightEntryEntry.TABLE_NAME +
                " GROUP BY " + WeightEntryEntry.COLUMN_USER_ID +
                " ORDER BY " + WeightEntryEntry.COLUMN_DATE + " DESC) " + weightEntryTableAlias +
                " ON " + UserEntry.TABLE_NAME + "." + WooserContract.UserEntry._ID +
                " = " +
                weightEntryTableAlias + "." + WeightEntryEntry.COLUMN_USER_ID;

        String[] projection = {
                UserEntry.TABLE_NAME + "." + UserEntry._ID + " AS " + UserEntry.ALIAS_ID,
                UserEntry.COLUMN_NAME,
                UserEntry.COLUMN_EMAIL,
                UserEntry.COLUMN_BIRTHDAY,
                UserEntry.COLUMN_GENDER,
                UserEntry.COLUMN_AVATAR_PATH,
                UserEntry.COLUMN_BODY_STRUCTURE,
                UserEntry.COLUMN_LIFE_STYLE,
                UserEntry.COLUMN_HEIGHT_CM,
                UserEntry.COLUMN_HEIGHT_UNIT,
                UserEntry.COLUMN_WEIGHT_UNIT,
                weightEntryTableAlias + "." + WeightEntryEntry._ID + " AS " + WeightEntryEntry.ALIAS_ID,
                weightEntryTableAlias + "." + WeightEntryEntry.COLUMN_WEIGHT_KG,
                weightEntryTableAlias + "." + WeightEntryEntry.COLUMN_USER_ID,
                weightEntryTableAlias + "." + WeightEntryEntry.COLUMN_DATE,
                weightEntryTableAlias + "." + WeightEntryEntry.COLUMN_BMI
        };

        sortOrder = UserEntry.COLUMN_NAME + " ASC";

        return query(db, userTable, projection, selection, selectionArgs, sortOrder, null);
    }

}