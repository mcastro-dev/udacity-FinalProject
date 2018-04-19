package com.mistdev.wooser.createEntry;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.WeightUnits;

import java.util.Calendar;

/**
 * Created by mcastro on 10/03/17.
 */

class CreateEntryView extends MvcView implements ICreateEntryView {

    private Listener mListener;
    private EditText etWeight;
    private EditText etEntryDate;
    private EditText etEntryTime;
    private DatePickerDialog dialogEntryDate;
    private TimePickerDialog dialogEntryTime;
    private Calendar mEntryDate;

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mEntryDate.set(year, month, dayOfMonth);
            String date = DateParser.calendarToLocalizedDateString(mEntryDate);
            etEntryDate.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mEntryDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mEntryDate.set(Calendar.MINUTE, minute);
            String date = DateParser.calendarToLocalizedTimeString(mEntryDate);
            etEntryTime.setText(date);
        }
    };

    CreateEntryView(Context context, View root) {
        super(context, root);

        mEntryDate = Calendar.getInstance();

        setupWeightInput();
        setupDateInput();
        setupTimeInput();
        setupCreateEntryFab();
    }

    private void setupWeightInput() {
        etWeight = (EditText)getRootView().findViewById(R.id.et_entry_weight);
    }

    private void setupDateInput() {

        dialogEntryDate = new DatePickerDialog(
                getContext(), dateListener,
                mEntryDate.get(Calendar.YEAR),
                mEntryDate.get(Calendar.MONTH),
                mEntryDate.get(Calendar.DAY_OF_MONTH)
        );
        dialogEntryDate.getDatePicker().setMaxDate(mEntryDate.getTimeInMillis());

        etEntryDate = (EditText)getRootView().findViewById(R.id.et_entry_date);
        etEntryDate.setText(DateParser.calendarToLocalizedDateString(mEntryDate));
        etEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEntryDate.show();
            }
        });
    }

    private void setupTimeInput() {

        dialogEntryTime = new TimePickerDialog(
                getContext(),
                timeListener,
                mEntryDate.get(Calendar.HOUR_OF_DAY),
                mEntryDate.get(Calendar.MINUTE),
                true
        );

        etEntryTime = (EditText)getRootView().findViewById(R.id.et_entry_time);
        etEntryTime.setText(DateParser.calendarToLocalizedTimeString(mEntryDate));
        etEntryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEntryTime.show();
            }
        });
    }

    private void setupCreateEntryFab() {

        FloatingActionButton fabCreateEntry = (FloatingActionButton)getRootView().findViewById(R.id.fab_create_entry);
        fabCreateEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEntry();
            }
        });
    }

    private void createEntry() {

        if(!validateFields()) {
            return;
        }

        float weight = StringParser.stringToFloat(etWeight.getText().toString());
        float bmi = WooserCalculator.calculateBMI(weight, WooserCredentialManager.getInstance().getLoggedUser().getHeightCentimeters());
        long date = mEntryDate.getTimeInMillis();

        WooserCredentialManager credentialManager = WooserCredentialManager.getInstance();
        @WeightUnits.Def int weightUnit = credentialManager.getLoggedUser().getWeightUnit();

        if(weightUnit == WeightUnits.LIBRAS) {
            weight = WooserCalculator.calculateWeightKilograms(weight);
        }

        WeightEntry entry = new WeightEntry();
        entry.setUserId(WooserCredentialManager.getInstance().getLoggedUser().getId());
        entry.setWeightKg(weight);
        entry.setBmi(bmi);
        entry.setDate(date);

        if(mListener != null) {
            mListener.onCreateEntryClicked(entry);
        }
    }

    private boolean validateFields() {

        if(etWeight.getText().toString().isEmpty()) {
            etWeight.setError(getString(R.string.validation_empty_field));
            return false;
        }

        etWeight.setError(null);

        return true;
    }

    @Override
    public void setupActionBar(ActionBar actionBar) {
        configureActionBar(actionBar);
    }

    @Override
    public void onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
        throw new UnsupportedOperationException("Not implementd. Do you mean to add a menu to this view?");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                if(mListener != null) {
                    mListener.onHomeSelected();
                }
                return true;

        }

        return false;
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void showError(String message) {
        Snackbar.make(getRootView(), message, Snackbar.LENGTH_LONG).show();
    }

}
