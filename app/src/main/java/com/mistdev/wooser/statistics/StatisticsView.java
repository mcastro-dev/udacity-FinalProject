package com.mistdev.wooser.statistics;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WeightChartHandler;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.StatisticPeriod;

import java.util.ArrayList;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by mcastro on 28/03/17.
 */

class StatisticsView extends MvcView implements IStatisticsView {

    private Listener mListener;
    private @StatisticPeriod.Def int mSelectedStatisticPeriod = StatisticPeriod.ONE_WEEK;

    private LineChartView mChart;
    private Button btnOneWeek;
    private Button btnOneMonth;
    private Button btnSixMonths;
    private Button btnOneYear;

    StatisticsView(Context context, View root, StatisticsRecyclerAdapter adapter) {
        super(context, root);

        setupViews(adapter);
    }

    private void setupViews(StatisticsRecyclerAdapter adapter) {

        mChart = (LineChartView)getRootView().findViewById(R.id.chart);

        setupStatisticsPeriodButtons();
        setupEntriesRecyclerView(adapter);
    }

    private void setupEntriesRecyclerView(StatisticsRecyclerAdapter adapter) {

        RecyclerView rcyStatistics = (RecyclerView)getRootView().findViewById(R.id.rcy_statistics);
        rcyStatistics.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        rcyStatistics.setAdapter(adapter);
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public Toolbar getToolbar() {
        return (Toolbar)getRootView().findViewById(R.id.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mListener.onHomeSelected();
                return true;
        }
        return false;
    }

    @Override
    public void updateChart(ArrayList<WeightEntry> weightEntries) {
        WeightChartHandler.updateChart(getContext(), mChart, weightEntries);
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

    private void updateSelectedStatisticPeriod(int selectedPeriod) {

        if(mSelectedStatisticPeriod == selectedPeriod) {
            return;
        }

        WeightChartHandler.selectedStatisticPeriodChanged(getContext(), selectedPeriod, mSelectedStatisticPeriod,
                btnOneWeek, btnOneMonth, btnSixMonths, btnOneYear);

        mSelectedStatisticPeriod = selectedPeriod;
    }
}
