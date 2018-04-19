package com.mistdev.wooser.data.crud;

import android.content.Context;

import com.mistdev.wooser.data.database.WooserContract.PlanEntry;
import com.mistdev.wooser.data.models.Plan;

/**
 * Created by mcastro on 21/03/17.
 */

public class CRUDPlan extends CRUD<Plan> {

    public CRUDPlan(Context context) {
        super(context, PlanEntry.CONTENT_URI, Plan.class);
    }

}
