package com.mistdev.wooser.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mistdev.wooser.data.database.WooserContract.*;

/**
 * Created by mcastro on 04/03/17.
 */

public class WooserDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Wooser.db";
    public static final int DB_VERSION = 1;

    public WooserDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static String getDatabasePath(Context context) {
        return context.getDatabasePath(DB_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createTableUser());
        db.execSQL(createTableWeightEntry());
        db.execSQL(createTablePlan());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /* CREATE TABLES
     * --------------------------------------------------------------------------------------------------------*/
    private String createTableUser() {

        return "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID +                     " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserEntry.COLUMN_NAME +             " VARCHAR," +
                UserEntry.COLUMN_EMAIL +            " VARCHAR," +
                UserEntry.COLUMN_BIRTHDAY +         " INTEGER," +
                UserEntry.COLUMN_AVATAR_PATH +      " VARCHAR," +
                UserEntry.COLUMN_GENDER +           " INTEGER," +
                UserEntry.COLUMN_BODY_STRUCTURE +   " INTEGER," +
                UserEntry.COLUMN_LIFE_STYLE +       " INTEGER," +
                UserEntry.COLUMN_WEIGHT_UNIT +      " INTEGER," +
                UserEntry.COLUMN_HEIGHT_UNIT +      " INTEGER," +
                UserEntry.COLUMN_HEIGHT_CM +        " REAL" +
                ");";
    }

    private String createTableWeightEntry() {

        return "CREATE TABLE " + WeightEntryEntry.TABLE_NAME + " (" +
                WeightEntryEntry._ID +                  " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeightEntryEntry.COLUMN_USER_ID +       " INTEGER," +
                WeightEntryEntry.COLUMN_DATE +          " INTEGER," +
                WeightEntryEntry.COLUMN_WEIGHT_KG +     " REAL," +
                WeightEntryEntry.COLUMN_BMI +           " REAL," +

                "FOREIGN KEY(" + WeightEntryEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                ");";
    }

    private String createTablePlan() {

        return "CREATE TABLE " + PlanEntry.TABLE_NAME + " (" +
                PlanEntry._ID +                 " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlanEntry.COLUMN_USER_ID +      " INTEGER," +
                PlanEntry.COLUMN_START_DATE +   " INTEGER," +
                PlanEntry.COLUMN_START_WEIGHT + " REAL," +
                PlanEntry.COLUMN_GOAL_DATE +    " INTEGER," +
                PlanEntry.COLUMN_GOAL_WEIGHT +  " REAL," +

                "FOREIGN KEY(" + PlanEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")" +
                ");";
    }
}
