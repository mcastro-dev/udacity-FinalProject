package com.mistdev.wooser.weight;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mistdev.mvc.controller.MvcControllerFragment;
import com.mistdev.wooser.LoadersFactory;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.WooserIntentsHandler;
import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.StatisticPeriod;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightFragment extends MvcControllerFragment implements
        IWeightView.Listener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ENTRIES = 0;
    private static final int LOADER_PLAN = 1;

    private static final int DAYS_ONE_WEEK = 7;
    private static final int MAX_HOUR = 23;
    private static final int MAX_MIN_SEC_MILLI = 59;
    private static final int MAX_PERCENTAGE = 100;

    private IWeightView iView;
    private WooserCredentialManager mWooserCredentialManager = WooserCredentialManager.getInstance();

    private Calendar mCurrentDate = Calendar.getInstance();
    private Calendar mInitialDate = Calendar.getInstance();
    private ArrayList<WeightEntry> mWeightEntries = new ArrayList<>();
    private Plan mPlan;

    public WeightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureCurrentDate();
        configureInitialDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return onCreateView(inflater, container, R.layout.fragment_weight);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ENTRIES, null, this).forceLoad();
        getLoaderManager().initLoader(LOADER_PLAN, null, this).forceLoad();
    }

    @Override
    public void setupView(View view) {
        super.setupView(view);

        iView = new WeightView(getContext(), view);
        iView.setListener(this);
    }

    @Override
    public void onCreatePlanClicked() {
        WooserIntentsHandler.openCreatePlan(getActivity());
    }

    @Override
    public void onEditPlanClicked() {

        if(mPlan != null) {
            WooserIntentsHandler.openCreatePlan(getActivity(), mPlan);
            return;
        }
        WooserIntentsHandler.openCreatePlan(getActivity());
    }

    @Override
    public void onCreateWeightEntryClicked() {
        WooserIntentsHandler.openCreateWeightEntryActivity(getActivity());
    }

    @Override
    public void onStatisticOneWeek() {
        updateInitialDate(StatisticPeriod.ONE_WEEK);
    }

    @Override
    public void onStatisticOneMonth() {
        updateInitialDate(StatisticPeriod.ONE_MONTH);
    }

    @Override
    public void onStatisticSixMonths() {
        updateInitialDate(StatisticPeriod.SIX_MONTHS);
    }

    @Override
    public void onStatisticOneYear() {
        updateInitialDate(StatisticPeriod.ONE_YEAR);
    }

    @Override
    public void onStatisticsDetailsClicked() {
        WooserIntentsHandler.openStatistics(getActivity());
    }

    @Override
    public int getWeightProgress() {
        if(mPlan == null) {
            return 0;
        }

        float currentWeight = WooserCredentialManager.getInstance().getLoggedUser().getWeightKg();

        float weightCurrentDifference = currentWeight - mPlan.getStartWeight();
        float weightGoalDifference = mPlan.getGoalWeight() - mPlan.getStartWeight();

        if(weightGoalDifference == 0) {
            return MAX_PERCENTAGE;
        }

        float progress = (weightCurrentDifference * 100) / weightGoalDifference;
        return fixProgressPercentage(progress);
    }

    @Override
    public int getDateProgress() {
        if(mPlan == null) {
            return 0;
        }

        Calendar currentDate = Calendar.getInstance();

        long millisStartDifference = mPlan.getGoalDate() - mPlan.getStartDate();
        long millisCurrentDifference = currentDate.getTimeInMillis() - mPlan.getStartDate();

        if(millisStartDifference == 0) {
            return MAX_PERCENTAGE;
        }

        long progress = (millisCurrentDifference * 100) / millisStartDifference;
        return fixProgressPercentage(progress);
    }

    //Fix to prevent the NumberBar from not updating.
    private int fixProgressPercentage(float progress) {

        if(progress < 0) {
            progress = 0;

        } else if(progress > MAX_PERCENTAGE) {
            progress = MAX_PERCENTAGE;
        }

        return (int)progress;
    }

    private void configureCurrentDate() {

        mCurrentDate.set(Calendar.HOUR_OF_DAY, MAX_HOUR);
        mCurrentDate.set(Calendar.MINUTE, MAX_MIN_SEC_MILLI);
        mCurrentDate.set(Calendar.SECOND, MAX_MIN_SEC_MILLI);
        mCurrentDate.set(Calendar.MILLISECOND, MAX_MIN_SEC_MILLI);
    }

    private void configureInitialDate() {

        mInitialDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
        mInitialDate.add(Calendar.DAY_OF_MONTH, - DAYS_ONE_WEEK);
    }

    private void updateInitialDate(@StatisticPeriod.Def int period) {

        mInitialDate.setTimeInMillis(mCurrentDate.getTimeInMillis());

        switch (period) {

            case StatisticPeriod.ONE_WEEK:
                mInitialDate.add(Calendar.DAY_OF_MONTH, - DAYS_ONE_WEEK);
                break;

            case StatisticPeriod.ONE_MONTH:
                mInitialDate.add(Calendar.MONTH, - 1);
                break;

            case StatisticPeriod.SIX_MONTHS:
                mInitialDate.add(Calendar.MONTH, - 6);
                break;

            case StatisticPeriod.ONE_YEAR:
                mInitialDate.add(Calendar.YEAR, - 1);
                break;
        }

        getLoaderManager().restartLoader(LOADER_ENTRIES, null, this).forceLoad();
    }


    /* LOADER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case LOADER_ENTRIES:
                return getEntriesLoader();

            case LOADER_PLAN:
                return getPlanLoader();

            default:
                throw new RuntimeException("Couldn't create loader with id: " + String.valueOf(id));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {

            case LOADER_ENTRIES:
                onLoadFinishedEntries(data);
                break;

            case LOADER_PLAN:
                onLoadFinishedPlan(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        switch (loader.getId()) {

            case LOADER_ENTRIES:

                mWeightEntries = new ArrayList<>();
                iView.updateChart(mWeightEntries);
                break;

            case LOADER_PLAN:

                mPlan = null;
                iView.initializePlan(null);
                break;
        }
    }

    private CursorLoader getEntriesLoader() {

        return LoadersFactory.buildWeightEntryLoader(
                getContext(),
                mWooserCredentialManager.getLoggedUser().getId(),
                mInitialDate.getTimeInMillis(),
                mCurrentDate.getTimeInMillis()
        );
    }

    private CursorLoader getPlanLoader() {

        return LoadersFactory.buildPlanLoader(getContext(), mWooserCredentialManager.getLoggedUser().getId());
    }

    private void onLoadFinishedEntries(Cursor data) {

        mWeightEntries = new ArrayList<>();

        if(data != null && data.moveToFirst()) {

            do {
                WeightEntry weightEntry = new WeightEntry().fromCursor(data);
                mWeightEntries.add(weightEntry);

            } while (data.moveToNext());
        }

        WeightEntry latestEntry = (mWeightEntries.size() > 0) ? mWeightEntries.get(0) : null;
        WeightEntry previousEntry = (mWeightEntries.size() > 1) ? mWeightEntries.get(1) : null;

        updateLatestEntry(latestEntry, previousEntry);
        iView.updateChart(mWeightEntries);
        iView.updatePlanProgress();
    }

    private void onLoadFinishedPlan(Cursor data) {

        //For now there is only one plan per user
        if(data != null && data.moveToFirst()) {
            mPlan = new Plan().fromCursor(data);
        }

        iView.initializePlan(mPlan);
    }

    private void updateLatestEntry(WeightEntry latestEntry, WeightEntry previousEntry) {
        //TODO: crash happens here after importing the database from GoogleDrive
        /*if(latestEntry == null) {
            return;
        }*/
        float weightVariation = calculateWeightVariationText(latestEntry, previousEntry);
        iView.updateLatestEntry(latestEntry.getDate(), latestEntry.getWeightKg(), latestEntry.getBmi(), weightVariation);
    }

    private float calculateWeightVariationText(WeightEntry latestEntry, WeightEntry previousEntry) {

        if(latestEntry == null || previousEntry == null) {
            return 0f;
        }

        float previousWeight;
        float currentWeight;

        if(latestEntry.getDate() > previousEntry.getDate()) {
            previousWeight = previousEntry.getWeightKg();
            currentWeight = latestEntry.getWeightKg();

        } else {
            previousWeight = latestEntry.getWeightKg();
            currentWeight = previousEntry.getWeightKg();
        }

        return currentWeight - previousWeight;
    }
}
