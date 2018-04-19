package com.mistdev.android_extensions.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mistdev.android_extensions.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mcastro on 16/03/17.
 */

public class SimpleDatePicker extends DialogFragment {

    private static final int LAST_MONTH_INDEX = 11;
    private static final String ARG_DEFAULT_YEAR = "arg_default_year";
    private static final String ARG_DEFAULT_MONTH = "arg_default_month";
    private static final String KEY_SAVED_CURRENT_YEAR = "key_saved_current_year";
    private static final String KEY_SAVED_CURRENT_MONTH = "key_saved_current_month";

    private SimpleDatePickerListener mListener;
    private int mCurrentYear;
    private int mCurrentMonth;
    private TextView txtYear;
    private TextView txtMonth;

    public static SimpleDatePicker newInstance(int defaultYear, int defaultMonth) {

        Bundle args = new Bundle();
        args.putInt(ARG_DEFAULT_YEAR, defaultYear);
        args.putInt(ARG_DEFAULT_MONTH, defaultMonth);

        SimpleDatePicker fragment = new SimpleDatePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();
        handleSavedInstanceState(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    private void handleArguments() {

        Bundle args = getArguments();
        if(args == null) {
            throw new RuntimeException("Must instantiate via newInstance() passing arguments.");
        }

        mCurrentYear = args.getInt(ARG_DEFAULT_YEAR);
        mCurrentMonth = args.getInt(ARG_DEFAULT_MONTH);
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            return;
        }

        if(savedInstanceState.containsKey(KEY_SAVED_CURRENT_YEAR)) {
            mCurrentYear = savedInstanceState.getInt(KEY_SAVED_CURRENT_YEAR);
        }

        if(savedInstanceState.containsKey(KEY_SAVED_CURRENT_MONTH)) {
            mCurrentMonth = savedInstanceState.getInt(KEY_SAVED_CURRENT_MONTH);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(KEY_SAVED_CURRENT_YEAR, mCurrentYear);
        outState.putInt(KEY_SAVED_CURRENT_MONTH, mCurrentMonth);
        super.onSaveInstanceState(outState);
    }

    public void setListener(SimpleDatePickerListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_simple_datepicker, container, false);

        setupTexts(view);
        setupButtons(view);

        return view;
    }

    private void setupTexts(View view) {

        txtMonth = (TextView)view.findViewById(R.id.sdp_txt_month);
        txtYear = (TextView)view.findViewById(R.id.sdp_txt_year);

        txtMonth.setText(getAbbreviatedMonth());
        txtYear.setText(String.valueOf(mCurrentYear));
    }

    private void setupButtons(View view) {

        Button btnCancel = (Button)view.findViewById(R.id.sdp_btn_cancel);
        Button btnOk = (Button)view.findViewById(R.id.sdp_btn_ok);
        Button btnAddMonth = (Button)view.findViewById(R.id.sdp_btn_add_month);
        Button btnSubtractMonth = (Button)view.findViewById(R.id.sdp_btn_subtract_month);
        Button btnAddYear = (Button)view.findViewById(R.id.sdp_btn_add_year);
        Button btnSubtractYear = (Button)view.findViewById(R.id.sdp_btn_subtract_year);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null) {
                    mListener.onDatePicked(mCurrentYear, mCurrentMonth);
                }
                dismiss();
            }
        });

        btnAddMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMonth();
            }
        });

        btnSubtractMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractMonth();
            }
        });

        btnAddYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addYear();
            }
        });

        btnSubtractYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractYear();
            }
        });
    }



    private void addMonth() {

        if(mCurrentMonth >= LAST_MONTH_INDEX) {
            mCurrentMonth = 0;

        } else {
            mCurrentMonth++;
        }

        txtMonth.setText(getAbbreviatedMonth());
    }

    private void subtractMonth() {

        if(mCurrentMonth <= 0) {
            mCurrentMonth = LAST_MONTH_INDEX;

        } else {
            mCurrentMonth--;
        }

        txtMonth.setText(getAbbreviatedMonth());
    }

    private void addYear() {

        if(mCurrentYear == 1) {
            return;
        }

        mCurrentYear++;
        txtYear.setText(String.valueOf(mCurrentYear));
    }

    private void subtractYear() {

        if(mCurrentYear == Integer.MAX_VALUE) {
            return;
        }

        mCurrentYear--;
        txtYear.setText(String.valueOf(mCurrentYear));
    }

    private String getAbbreviatedMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mCurrentMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    public interface SimpleDatePickerListener {
        void onDatePicked(int year, int monthOfYear);
    }
}
