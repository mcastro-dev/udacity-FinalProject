package com.mistdev.wooser.createPlan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.crud.CRUDPlan;
import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.User;

import java.util.Calendar;

public class CreatePlanActivity extends MvcControllerActivity implements ICreatePlanView.Listener {

    public static final String ARG_PLAN = "arg_plan";

    private ICreatePlanView iView;
    private Plan mPlan;
    private boolean inEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();

        onCreate(R.layout.activity_create_plan);
    }

    private void handleIntent() {
        Intent intent = getIntent();

        if(intent == null) {
            return;
        }

        if(intent.hasExtra(ARG_PLAN)) {
            mPlan = intent.getParcelableExtra(ARG_PLAN);
            inEditMode = true;
        }
    }

    @Override
    public void setupView() {
        super.setupView();

        User loggedUser = WooserCredentialManager.getInstance().getLoggedUser();

        iView = new CreatePlanView(this, findViewById(android.R.id.content));
        iView.setListener(this);

        if(!inEditMode) {
            iView.setStartWeight(loggedUser.getWeightKg());
            return;
        }

        iView.setStartWeight(mPlan.getStartWeight());
        iView.setStartDate(mPlan.getStartDate());
        iView.setGoalWeight(mPlan.getGoalWeight());
        iView.setGoalDate(mPlan.getGoalDate());
    }

    @Override
    public void setupActionBar() {
        super.setupActionBar();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
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
    public void onConfirmPlan(Plan plan) {

        if(inEditMode) {
            updatePlan(plan);
            return;
        }

        createPlan(plan);
    }

    private void createPlan(Plan plan) {

        plan = adaptPlanDates(plan);

        iView.showLoading();

        try {

            CRUDPlan crudPlan = new CRUDPlan(this);
            crudPlan.create(plan, new CRUD.CreateCallback() {
                @Override
                public void onCreateSuccess(long objId) {
                    iView.hideLoading();
                    finish();
                }

                @Override
                public void onCreateFail() {
                    iView.hideLoading();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePlan(Plan plan) {

        plan.setId(mPlan.getId());
        plan = adaptPlanDates(plan);

        iView.showLoading();

        try {

            CRUDPlan crudPlan = new CRUDPlan(this);
            crudPlan.update(mPlan.getId(), plan, new CRUD.UpdateCallback() {
                @Override
                public void onUpdateSuccess(int updateCount) {
                    iView.hideLoading();
                    finish();
                }

                @Override
                public void onUpdateFail() {
                    iView.hideLoading();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Plan adaptPlanDates(Plan plan) {

        plan.setStartDate(adaptDate(plan.getStartDate()));
        plan.setGoalDate(adaptDate(plan.getGoalDate()));

        return plan;
    }

    private long adaptDate(long date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
