package com.mistdev.wooser;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.mistdev.wooser.data.database.WooserContract;

/**
 * Created by mcastro on 01/04/17.
 */

public class LoadersFactory {

    public static CursorLoader buildWeightEntryLoader(Context context, long userId, long initialDate, long finalDate) {

        Uri uri = WooserContract.WeightEntryEntry.CONTENT_URI;

        String selection = WooserContract.WeightEntryEntry.COLUMN_USER_ID + "=? AND " +
                WooserContract.WeightEntryEntry.COLUMN_DATE + " >= ? AND " + WooserContract.WeightEntryEntry.COLUMN_DATE + " <= ?";

        String[] selectionArgs = {
                String.valueOf(userId),
                String.valueOf(initialDate),
                String.valueOf(finalDate)
        };
        String sortOrder = WooserContract.WeightEntryEntry.COLUMN_DATE + " DESC";

        return new CursorLoader(context, uri, null, selection, selectionArgs, sortOrder);
    }

    public static CursorLoader buildPlanLoader(Context context, long userId) {

        Uri uri = WooserContract.PlanEntry.CONTENT_URI;

        String selection = WooserContract.PlanEntry.COLUMN_USER_ID + "=?";
        String[] selectionArgs = { String.valueOf(userId) };

        return new CursorLoader(context, uri, null, selection, selectionArgs, null);
    }

}
