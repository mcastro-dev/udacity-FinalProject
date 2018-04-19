package com.mistdev.wooser.data.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mcastro on 04/03/17.
 */

public class WooserContract {

    static final String CONTENT_AUTHORITY = "com.mistdev.wooser";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Accessible paths
    static final String PATH_USER = "user";
    static final String PATH_WEIGHT_ENTRY = "weight_entry";
    static final String PATH_PLAN = "plan";

    public static class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        //table
        public static final String TABLE_NAME = "user";

        //columns
        public static final String COLUMN_NAME = "user_name";
        public static final String COLUMN_EMAIL = "user_email";
        public static final String COLUMN_BIRTHDAY = "user_birthday";
        public static final String COLUMN_AVATAR_PATH = "user_avatar_path";
        public static final String COLUMN_GENDER = "user_gender";
        public static final String COLUMN_BODY_STRUCTURE = "user_body_structure";
        public static final String COLUMN_LIFE_STYLE = "user_life_style";
        public static final String COLUMN_WEIGHT_UNIT = "user_weight_unit";
        public static final String COLUMN_HEIGHT_UNIT = "user_height_unit";
        public static final String COLUMN_HEIGHT_CM = "user_height_cm";

        //Alias
        public static final String ALIAS_ID = "user_entry_id";

        //Joined
        public static final String JOINED_WEIGHT_KG = WeightEntryEntry.COLUMN_WEIGHT_KG;
        public static final String JOINED_WEIGHT_USER_ID = WeightEntryEntry.COLUMN_USER_ID;
        public static final String JOINED_WEIGHT_DATE = WeightEntryEntry.COLUMN_DATE;
        public static final String JOINED_WEIGHT_BMI = WeightEntryEntry.COLUMN_BMI;

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class WeightEntryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEIGHT_ENTRY).build();

        //table
        public static final String TABLE_NAME = "weight_entry";

        //columns
        public static final String COLUMN_USER_ID = "weight_entry_user_id";
        public static final String COLUMN_DATE = "weight_entry_date";
        public static final String COLUMN_WEIGHT_KG = "weight_entry_weight_kg";
        public static final String COLUMN_BMI = "weight_entry_bmi";

        //Alias
        public static final String ALIAS_ID = "weight_entry_id";

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class PlanEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAN).build();

        //table
        public static final String TABLE_NAME = "plan";

        //columns
        public static final String COLUMN_USER_ID = "plan_user_id";
        public static final String COLUMN_START_DATE = "plan_start_date";
        public static final String COLUMN_START_WEIGHT = "plan_start_weight";
        public static final String COLUMN_GOAL_DATE = "plan_goal_date";
        public static final String COLUMN_GOAL_WEIGHT = "plan_goal_weight";

        public static Uri buildUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
