package com.mistdev.wooser.weight;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.BmiClassificationHandler;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WeightChartHandler;
import com.mistdev.wooser.WeightHelper;
import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.StatisticPeriod;
import com.mistdev.wooser.enums.WeightUnits;

import java.util.ArrayList;
import java.util.Calendar;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by mcastro on 18/03/17.
 */

class WeightView extends MvcView implements IWeightView {

    private Listener mListener;
    private Plan mPlan;
    private @StatisticPeriod.Def int mSelectedStatisticPeriod = StatisticPeriod.ONE_WEEK;
    private WooserCredentialManager mCredentialManager = WooserCredentialManager.getInstance();

    private LineChartView mLineChart;
    private Button btnOneWeek;
    private Button btnOneMonth;
    private Button btnSixMonths;
    private Button btnOneYear;

    WeightView(Context context, View root) {
        super(context, root);

        setupViews();
    }

    private void setupViews() {

        mLineChart = (LineChartView) getRootView().findViewById(R.id.chart);

        setupCreateWeightEntryFab();
        setupCreatePlanButton();
        setupEditPlanButton();
        setupStatisticsPeriodButtons();
        setupStatisticsDetailsButton();
    }

    private void setupCreatePlanButton() {

        Button btnCreatePlan = (Button)getRootView().findViewById(R.id.btn_create_plan);
        btnCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) {
                    mListener.onCreatePlanClicked();
                }
            }
        });
    }

    private void setupEditPlanButton() {

        Button btnEditPlan = (Button)getRootView().findViewById(R.id.btn_edit_plan);
        btnEditPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) {
                    mListener.onEditPlanClicked();
                }
            }
        });
    }

    private void setupCreateWeightEntryFab() {

        final FloatingActionButton fabCreateWeightEntry = (FloatingActionButton)getRootView().findViewById(R.id.fab_create_weight_entry);
        fabCreateWeightEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) {
                    mListener.onCreateWeightEntryClicked();
                }
            }
        });

        final ScrollView scrollView = (ScrollView)getRootView().findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                int scrollY = scrollView.getScrollY();

                if(scrollY > 0) {
                    fabCreateWeightEntry.hide();

                } else if(scrollY <= 0) {
                    fabCreateWeightEntry.show();
                }
            }
        });
    }

    private void setupStatisticsPeriodButtons() {

        btnOneWeek = (Button)getRootView().findViewById(R.id.btn_one_week);
        btnOneMonth = (Button)getRootView().findViewById(R.id.btn_one_month);
        btnSixMonths = (Button)getRootView().findViewById(R.id.btn_six_months);
        btnOneYear = (Button)getRootView().findViewById(R.id.btn_one_year);

        btnOneWeek.setTextColor(ContextCompat.getColor(getContext(), WeightChartHandler.statisticSelectedPeriodColor));

        btnOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedStatisticPeriod == StatisticPeriod.ONE_WEEK) {
                    return;
                }
                updateSelectedStatisticPeriod(StatisticPeriod.ONE_WEEK);
                mListener.onStatisticOneWeek();
            }
        });

        btnOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedStatisticPeriod == StatisticPeriod.ONE_MONTH) {
                    return;
                }
                updateSelectedStatisticPeriod(StatisticPeriod.ONE_MONTH);
                mListener.onStatisticOneMonth();
            }
        });

        btnSixMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedStatisticPeriod == StatisticPeriod.SIX_MONTHS) {
                    return;
                }
                updateSelectedStatisticPeriod(StatisticPeriod.SIX_MONTHS);
                mListener.onStatisticSixMonths();
            }
        });

        btnOneYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedStatisticPeriod == StatisticPeriod.ONE_YEAR) {
                    return;
                }
                updateSelectedStatisticPeriod(StatisticPeriod.ONE_YEAR);
                mListener.onStatisticOneYear();
            }
        });
    }

    private void setupStatisticsDetailsButton() {

        Button btnStatisticsDetails = (Button)getRootView().findViewById(R.id.btn_statistics_details);
        btnStatisticsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) {
                    mListener.onStatisticsDetailsClicked();
                }
            }
        });
    }

    private void updateSelectedStatisticPeriod(int selectedPeriod) {

        if(mSelectedStatisticPeriod == selectedPeriod) {
            return;
        }

        WeightChartHandler.selectedStatisticPeriodChanged(getContext(), selectedPeriod, mSelectedStatisticPeriod,
                btnOneWeek, btnOneMonth, btnSixMonths, btnOneYear);

        mSelectedStatisticPeriod = selectedPeriod;
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void updateChart(final ArrayList<WeightEntry> weightEntries) {
        WeightChartHandler.updateChart(getContext(), mLineChart, weightEntries);
    }

    @Override
    public void initializePlan(Plan plan) {
        if(plan == null) {
            return;
        }

        mPlan = plan;

        View cardEmptyPlan = getRootView().findViewById(R.id.card_empty_plan);
        View cardPlan = getRootView().findViewById(R.id.card_plan);
        TextView txtStartDate = (TextView)getRootView().findViewById(R.id.txt_start_date);
        TextView txtStartWeight = (TextView)getRootView().findViewById(R.id.txt_start_weight);
        TextView txtGoalDate = (TextView)getRootView().findViewById(R.id.txt_goal_date);
        TextView txtGoalWeight = (TextView)getRootView().findViewById(R.id.txt_goal_weight);

        cardEmptyPlan.setVisibility(View.GONE);
        cardPlan.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(plan.getStartDate());
        txtStartDate.setText(DateParser.calendarToLocalizedDateString(calendar));

        calendar.setTimeInMillis(plan.getGoalDate());
        txtGoalDate.setText(DateParser.calendarToLocalizedDateString(calendar));

        float startWeightKg = plan.getStartWeight();
        float goalWeightKg = plan.getGoalWeight();

        @WeightUnits.Def int weightUnit = mCredentialManager.getLoggedUser().getWeightUnit();

        String startWeightText = WeightHelper.formatWeightText(getContext(), weightUnit, startWeightKg);
        String goalWeightText = WeightHelper.formatWeightText(getContext(), weightUnit, goalWeightKg);

        txtStartWeight.setText(startWeightText);
        txtGoalWeight.setText(goalWeightText);

        updatePlanProgress();
    }

    @Override
    public void updatePlanProgress() {
        if(mPlan == null) {
            return;
        }

        NumberProgressBar npbWeightProgress = (NumberProgressBar) getRootView().findViewById(R.id.npb_weight_progress);
        NumberProgressBar npbDateProgress = (NumberProgressBar) getRootView().findViewById(R.id.npb_date_progress);

        npbWeightProgress.setProgress(mListener.getWeightProgress());
        npbDateProgress.setProgress(mListener.getDateProgress());
    }

    @Override
    public void updateLatestEntry(long date, float weightKg, float bmi, float weightKgVariation) {

        TextView txtLatestEntryDate = (TextView)getRootView().findViewById(R.id.txt_date);
        TextView txtLatestWeight = (TextView)getRootView().findViewById(R.id.txt_weight);
        TextView txtLatestWeightVariation = (TextView)getRootView().findViewById(R.id.txt_weight_variation);
        TextView txtLatestBmi = (TextView)getRootView().findViewById(R.id.txt_bmi);
        TextView txtLatestBmiClassification = (TextView)getRootView().findViewById(R.id.txt_bmi_classification);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        @WeightUnits.Def int weightUnit = mCredentialManager.getLoggedUser().getWeightUnit();

        String weightText = WeightHelper.formatWeightText(getContext(), weightUnit, weightKg);
        String weightVariationText = WeightHelper.formatWeightText(getContext(), weightUnit, weightKgVariation);

        String dateText = DateParser.calendarToLocalizedDateTimeString(calendar);
        String bmiText = String.format(
                getContext().getString(R.string.format_bmi),
                StringParser.floatToString(bmi, WooserCalculator.BMI_DEFAULT_DECIMAL_PLACES)
        );

        txtLatestEntryDate.setText(dateText);
        txtLatestWeight.setText(weightText);
        txtLatestBmi.setText(bmiText);
        txtLatestWeightVariation.setText(weightVariationText);

        BmiClassificationHandler.setupBmiClassificationTextView(getContext(), txtLatestBmiClassification, bmi);
    }

}
