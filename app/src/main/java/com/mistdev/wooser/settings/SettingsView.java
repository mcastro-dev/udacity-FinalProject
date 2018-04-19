package com.mistdev.wooser.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.WeightUnits;

/**
 * Created by mcastro on 01/04/17.
 */

class SettingsView extends MvcView implements ISettingsView {

    private @WeightUnits.Def int mWeightUnit = WooserCredentialManager.getInstance().getLoggedUser().getWeightUnit();
    private @WeightUnits.Def int mHeightUnit = WooserCredentialManager.getInstance().getLoggedUser().getHeightUnit();

    private Listener mListener;
    private Spinner spnWeightUnit;
    private Spinner spnHeightUnit;

    SettingsView(Context context, View root) {
        super(context, root);

        setupViews();
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void setupViews() {

        setupEditUserButton();
        setupDeleteUserButton();
        setupWeightUnitDropdown();
        setupHeightUnitDropdown();
    }

    private void setupEditUserButton() {

        Button btnEditUser = (Button)getRootView().findViewById(R.id.btn_edit_user);
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditUser();
            }
        });
    }

    private void setupDeleteUserButton() {

        Button btnDeleteUser = (Button)getRootView().findViewById(R.id.btn_delete_user);
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteUserDialog();
            }
        });
    }

    private void setupWeightUnitDropdown() {

        ArrayAdapter<String> adapter = getArrayAdapter(R.array.weight_units);
        spnWeightUnit = (Spinner)getRootView().findViewById(R.id.spn_weight_unit);
        spnWeightUnit.setAdapter(adapter);
        spnWeightUnit.setSelection(mWeightUnit);

        spnWeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                @WeightUnits.Def int weightUnit = position;
                mListener.updateWeightUnit(weightUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupHeightUnitDropdown() {

        ArrayAdapter<String> adapter = getArrayAdapter(R.array.height_units);
        spnHeightUnit = (Spinner)getRootView().findViewById(R.id.spn_height_unit);
        spnHeightUnit.setAdapter(adapter);
        spnHeightUnit.setSelection(mHeightUnit);

        spnHeightUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                @HeightUnits.Def int heightUnit = position;
                mListener.updateHeightUnit(heightUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private ArrayAdapter<String> getArrayAdapter(@ArrayRes int arrayRes) {

        String[] array = getContext().getResources().getStringArray(arrayRes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void showDeleteUserDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle(getContext().getString(R.string.delete_user_dialog_title))
                .setMessage(getContext().getString(R.string.delete_user_dialog_message))
                .setNegativeButton(getContext().getString(android.R.string.cancel), null)
                .setPositiveButton(getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDeleteUser();
                    }
                })
                .create()
                .show();
    }
}
