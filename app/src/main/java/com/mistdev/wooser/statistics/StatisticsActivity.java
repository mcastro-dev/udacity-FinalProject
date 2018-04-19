package com.mistdev.wooser.statistics;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.LoadersFactory;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.StatisticPeriod;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsActivity extends MvcControllerActivity implements
        IStatisticsView.Listener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_STATISTICS = 0;
    private static final int ONE_WEEK_DAYS = 7;
    private static final int ONE_MONTH = 1;
    private static final int SIX_MONTHS = 6;
    private static final int ONE_YEAR = 1;

    private IStatisticsView iView;
    private StatisticsRecyclerAdapter mAdapter;
    private WooserCredentialManager mWooserCredentialManager = WooserCredentialManager.getInstance();
    private Calendar mInitialDate = Calendar.getInstance();
    private ArrayList<WeightEntry> mWeightEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_statistics);

        updateInitialDate(StatisticPeriod.ONE_WEEK);

        getSupportLoaderManager().initLoader(LOADER_STATISTICS, null, this).forceLoad();
    }

    @Override
    public void setupView() {

        mAdapter = new StatisticsRecyclerAdapter(null);
        iView = new StatisticsView(this, findViewById(android.R.id.content), mAdapter);
        iView.setListener(this);
    }

    @Override
    public void setupActionBar() {

        setSupportActionBar(iView.getToolbar());

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return iView.onOptionsItemSelected(item);
    }

    @Override
    public void onHomeSelected() {
        finish();
    }

    @Override
    public void onStatisticOneWeek() {

        updateInitialDate(StatisticPeriod.ONE_WEEK);
        restartStatisticLoader();
    }

    @Override
    public void onStatisticOneMonth() {

        updateInitialDate(StatisticPeriod.ONE_MONTH);
        restartStatisticLoader();
    }

    @Override
    public void onStatisticSixMonths() {

        updateInitialDate(StatisticPeriod.SIX_MONTHS);
        restartStatisticLoader();
    }

    @Override
    public void onStatisticOneYear() {

        updateInitialDate(StatisticPeriod.ONE_YEAR);
        restartStatisticLoader();
    }

    private void updateInitialDate(@StatisticPeriod.Def int statisticPeriod) {

        mInitialDate = Calendar.getInstance();

        switch (statisticPeriod) {

            case StatisticPeriod.ONE_WEEK:
                mInitialDate.set(Calendar.DAY_OF_MONTH, - ONE_WEEK_DAYS);
                break;

            case StatisticPeriod.ONE_MONTH:
                mInitialDate.set(Calendar.MONTH, - ONE_MONTH);
                break;

            case StatisticPeriod.SIX_MONTHS:
                mInitialDate.set(Calendar.MONTH, - SIX_MONTHS);
                break;

            case StatisticPeriod.ONE_YEAR:
                mInitialDate.set(Calendar.YEAR, - ONE_YEAR);
                break;
        }
    }

    private void restartStatisticLoader() {
        getSupportLoaderManager().restartLoader(LOADER_STATISTICS, null, this).forceLoad();
    }

    /* LOADER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return LoadersFactory.buildWeightEntryLoader(
                this,
                mWooserCredentialManager.getLoggedUser().getId(),
                mInitialDate.getTimeInMillis(),
                Calendar.getInstance().getTimeInMillis()
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        changeAdapterCursor(data);

        mWeightEntries.clear();

        if(data != null && data.moveToFirst()) {

            do {
                WeightEntry weightEntry = new WeightEntry().fromCursor(data);
                mWeightEntries.add(weightEntry);

            } while (data.moveToNext());
        }

        iView.updateChart(mWeightEntries);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        changeAdapterCursor(null);
    }

    private void changeAdapterCursor(Cursor cursor) {

        if(mAdapter != null) {
            mAdapter.changeCursor(cursor);
        }
    }
}
