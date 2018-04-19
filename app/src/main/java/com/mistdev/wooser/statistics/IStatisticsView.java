package com.mistdev.wooser.statistics;

import android.support.v7.widget.Toolbar;

import com.mistdev.mvc.view.IOptionsMenu;
import com.mistdev.wooser.data.models.WeightEntry;

import java.util.ArrayList;

/**
 * Created by mcastro on 28/03/17.
 */

interface IStatisticsView extends IOptionsMenu {

    interface Listener {
        void onHomeSelected();
        void onStatisticOneWeek();
        void onStatisticOneMonth();
        void onStatisticSixMonths();
        void onStatisticOneYear();
    }

    void setListener(Listener listener);
    void updateChart(ArrayList<WeightEntry> weightEntries);
    Toolbar getToolbar();
}
