package com.mistdev.wooser.settings;

import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.WeightUnits;

/**
 * Created by mcastro on 01/04/17.
 */

interface ISettingsView {

    interface Listener {
        void updateWeightUnit(@WeightUnits.Def int weightUnit);
        void updateHeightUnit(@HeightUnits.Def int heightUnit);
        void onEditUser();
        void onDeleteUser();
    }

    void setListener(Listener listener);
}
