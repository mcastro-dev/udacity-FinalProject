package com.mistdev.wooser.data.crud;

import android.content.Context;

import com.mistdev.wooser.data.database.WooserContract;
import com.mistdev.wooser.data.models.WeightEntry;

/**
 * Created by mcastro on 09/03/17.
 */

public class CRUDWeightEntry extends CRUD<WeightEntry> {

    public CRUDWeightEntry(Context context) {
        super(context, WooserContract.WeightEntryEntry.CONTENT_URI, WeightEntry.class);
    }
}
