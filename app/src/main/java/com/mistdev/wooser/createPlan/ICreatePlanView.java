package com.mistdev.wooser.createPlan;

import com.mistdev.mvc.view.ILoading;
import com.mistdev.mvc.view.IOptionsMenu;
import com.mistdev.wooser.data.models.Plan;

/**
 * Created by mcastro on 20/03/17.
 */

interface ICreatePlanView extends IOptionsMenu, ILoading {

    interface Listener {
        void onHomeSelected();
        void onConfirmPlan(Plan plan);
    }

    void setListener(Listener listener);
    void setStartWeight(float weightKg);
    void setStartDate(long dateMillis);
    void setGoalWeight(float weightKg);
    void setGoalDate(long dateMillis);
}
