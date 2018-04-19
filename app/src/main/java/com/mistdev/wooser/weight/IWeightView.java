package com.mistdev.wooser.weight;

import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.WeightEntry;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mcastro on 18/03/17.
 */

interface IWeightView {

    interface Listener {
        void onCreatePlanClicked();
        void onEditPlanClicked();
        void onCreateWeightEntryClicked();
        int getWeightProgress();
        int getDateProgress();
        void onStatisticOneWeek();
        void onStatisticOneMonth();
        void onStatisticSixMonths();
        void onStatisticOneYear();
        void onStatisticsDetailsClicked();
    }

    void setListener(Listener listener);
    void initializePlan(Plan plan);
    void updateChart(ArrayList<WeightEntry> weightEntries);
    void updatePlanProgress();
    void updateLatestEntry(long date, float weightKg, float bmi, float weightKgVariation);
}
