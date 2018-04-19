package com.mistdev.wooser.createPlan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WeightHelper;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.User;

import java.util.Calendar;

/**
 * Created by mcastro on 20/03/17.
 */

class CreatePlanView extends MvcView implements ICreatePlanView {

    private Listener mListener;
    private Calendar mStartDate;
    private Calendar mGoalDate;

    private DatePickerDialog dialogStartDate;
    private DatePickerDialog dialogGoalDate;
    private EditText etStartDate;
    private EditText etGoalDate;
    private EditText etStartWeight;
    private EditText etGoalWeight;

    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mStartDate.set(year, month, dayOfMonth);
            String date = DateParser.calendarToLocalizedDateString(mStartDate);
            etStartDate.setText(date);
        }
    };

    private DatePickerDialog.OnDateSetListener goalDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mGoalDate.set(year, month, dayOfMonth);
            String date = DateParser.calendarToLocalizedDateString(mGoalDate);
            etGoalDate.setText(date);
        }
    };

    CreatePlanView(Context context, View root) {
        super(context, root);

        setupViews();
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void setStartWeight(float weightKg) {

        float weight = WeightHelper.convertKilogramsToLoggedUserWeightUnit(weightKg);
        etStartWeight.setText(String.valueOf(weight));
    }

    @Override
    public void setStartDate(long dateMillis) {

        mStartDate.setTimeInMillis(dateMillis);
        etStartDate.setText(DateParser.calendarToLocalizedDateString(mStartDate));
    }

    @Override
    public void setGoalWeight(float weightKg) {

        float weight = WeightHelper.convertKilogramsToLoggedUserWeightUnit(weightKg);
        etGoalWeight.setText(String.valueOf(weight));
    }

    @Override
    public void setGoalDate(long dateMillis) {

        mGoalDate.setTimeInMillis(dateMillis);
        etGoalDate.setText(DateParser.calendarToLocalizedDateString(mGoalDate));
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void setupViews() {

        etStartWeight = (EditText)getRootView().findViewById(R.id.et_start_weight);
        etGoalWeight = (EditText)getRootView().findViewById(R.id.et_goal_weight);
        setupStartDate();
        setupGoalDate();setupConfirmPlanFab();
    }

    private void setupStartDate() {

        mStartDate = Calendar.getInstance();

        dialogStartDate = new DatePickerDialog(
                getContext(), startDateListener,
                mStartDate.get(Calendar.YEAR),
                mStartDate.get(Calendar.MONTH),
                mStartDate.get(Calendar.DAY_OF_MONTH)
        );

        etStartDate = (EditText)getRootView().findViewById(R.id.et_start_date);
        etStartDate.setText(DateParser.calendarToLocalizedDateString(mStartDate));
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogStartDate.show();
            }
        });
    }

    private void setupGoalDate() {

        mGoalDate = Calendar.getInstance();

        dialogGoalDate = new DatePickerDialog(
                getContext(), goalDateListener,
                mGoalDate.get(Calendar.YEAR),
                mGoalDate.get(Calendar.MONTH),
                mGoalDate.get(Calendar.DAY_OF_MONTH)
        );

        etGoalDate = (EditText)getRootView().findViewById(R.id.et_goal_date);
        etGoalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGoalDate.show();
            }
        });
    }

    private void setupConfirmPlanFab() {

        FloatingActionButton fabConfirmPlan = (FloatingActionButton)getRootView().findViewById(R.id.fab_confirm_plan_creation);
        fabConfirmPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlan();
            }
        });

    }

    private void createPlan() {

        if(!areFieldsValid()) {
            return;
        }

        long userId = WooserCredentialManager.getInstance().getLoggedUser().getId();

        float startWeight = StringParser.stringToFloat(etStartWeight.getText().toString());
        float goalWeight = StringParser.stringToFloat(etGoalWeight.getText().toString());

        float startWeightKg = WeightHelper.convertLoggedUserWeightUnitToKilograms(startWeight);
        float goalWeightKg = WeightHelper.convertLoggedUserWeightUnitToKilograms(goalWeight);

        Plan plan = new Plan();
        plan.setUserId(userId);
        plan.setStartDate(mStartDate.getTimeInMillis());
        plan.setGoalDate(mGoalDate.getTimeInMillis());
        plan.setStartWeight(startWeightKg);
        plan.setGoalWeight(goalWeightKg);

        if(mListener != null) {
            mListener.onConfirmPlan(plan);
        }
    }

    private boolean areFieldsValid() {

        boolean validStartWeight = !hasFieldError(etStartWeight);
        boolean validGoalWeight = !hasFieldError(etGoalWeight);
        boolean validStartDate = !hasFieldError(etStartDate);
        boolean validGoalDate = !hasFieldError(etGoalDate);

        return validStartWeight && validGoalWeight && validStartDate && validGoalDate;
    }

    private boolean hasFieldError(EditText et) {

        if(et.getText().toString().trim().isEmpty()) {
            et.setError(getString(R.string.validation_empty_field));
            return true;
        }

        et.setError(null);

        return false;
    }
}
