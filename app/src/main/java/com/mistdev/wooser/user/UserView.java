package com.mistdev.wooser.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.dialogs.SelectionDialog;
import com.mistdev.wooser.enums.BodyStructure;
import com.mistdev.wooser.enums.Gender;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.LifeStyle;
import com.mistdev.wooser.enums.WeightUnits;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mcastro on 03/04/17.
 */

class UserView extends MvcView implements IUserView {

    private static final int DEFAULT_BIRTHDAY_YEAR = 1990;

    private Listener mListener;
    private Calendar mBirthday = Calendar.getInstance();

    private DatePickerDialog dialogBirthday;
    private EditText etName;
    private EditText etBirthday;
    private EditText etWeight;
    private EditText etHeight;
    private EditText etGender;
    private EditText etBodyStructure;
    private EditText etLifeStyle;
    private Spinner spnWeightUnit;
    private Spinner spnHeightUnit;

    private @Gender.Def int mSelectedGender = Gender.NONE;
    private @BodyStructure.Def int mSelectedBodyStructure = BodyStructure.NONE;
    private @LifeStyle.Def int mSelectedLifeStyle = LifeStyle.NONE;

    private DatePickerDialog.OnDateSetListener birthdayListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mBirthday.set(year, month, dayOfMonth);
            String date = DateParser.calendarToLocalizedDateString(mBirthday);
            etBirthday.setText(date);
        }
    };

    UserView(Context context, View root) {
        super(context, root);

        mBirthday.set(Calendar.YEAR, DEFAULT_BIRTHDAY_YEAR);

        setupViews();
    }

    private void setupViews() {

        spnWeightUnit = (Spinner)getRootView().findViewById(R.id.spn_weight_unit);
        spnHeightUnit = (Spinner)getRootView().findViewById(R.id.spn_height_unit);
        etName = (EditText)getRootView().findViewById(R.id.et_user_name);
        etBirthday = (EditText)getRootView().findViewById(R.id.et_birthday);
        etWeight = (EditText)getRootView().findViewById(R.id.et_user_weight);
        etHeight = (EditText)getRootView().findViewById(R.id.et_user_height);
        etGender = (EditText)getRootView().findViewById(R.id.et_user_gender);
        etBodyStructure = (EditText)getRootView().findViewById(R.id.et_body_structure);
        etLifeStyle = (EditText)getRootView().findViewById(R.id.et_lifestyle);

        FloatingActionButton fabConfirmUser = (FloatingActionButton)getRootView().findViewById(R.id.fab_confirm_user_creation);
        fabConfirmUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assembleUser();
            }
        });

        setupWeightUnitDropdown();
        setupHeightUnitDropdown();
        setupBirthday();

        etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.genderClicked();
            }
        });
        etBodyStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.bodyStructureClicked();
            }
        });
        etLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.lifeStyleClicked();
            }
        });
    }

    private void setupWeightUnitDropdown() {

        ArrayAdapter<String> adapter = getArrayAdapter(R.array.weight_units);
        spnWeightUnit.setAdapter(adapter);
    }

    private void setupHeightUnitDropdown() {

        ArrayAdapter<String> adapter = getArrayAdapter(R.array.height_units);
        spnHeightUnit.setAdapter(adapter);
    }

    private ArrayAdapter<String> getArrayAdapter(@ArrayRes int arrayRes) {

        String[] array = getContext().getResources().getStringArray(arrayRes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    void setupBirthday() {

        dialogBirthday = new DatePickerDialog(
                getContext(), birthdayListener,
                mBirthday.get(Calendar.YEAR),
                mBirthday.get(Calendar.MONTH),
                mBirthday.get(Calendar.DAY_OF_MONTH)
        );

        etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBirthday.show();
            }
        });
    }

    private void assembleUser() {

        if(!areFieldsValid()) {
            return;
        }

        User user = new User();
        user.setName(etName.getText().toString());
        user.setBirthday(mBirthday.getTimeInMillis());
        user.setGender(mSelectedGender);
        user.setBodyStructure(mSelectedBodyStructure);
        user.setLifeStyle(mSelectedLifeStyle);
        user.setWeightUnit(spnWeightUnit.getSelectedItemPosition());
        user.setHeightUnit(spnHeightUnit.getSelectedItemPosition());

        //Set height in centimeters
        if(spnHeightUnit.getSelectedItemPosition() == HeightUnits.FEET) {
            float heightFeet = StringParser.stringToFloat(etHeight.getText().toString());
            float heightCentimeters = WooserCalculator.calculateHeightCentimeters(heightFeet);
            user.setHeightCentimeters(heightCentimeters);

        } else {
            float heightCentimeters = StringParser.stringToFloat(etHeight.getText().toString());
            user.setHeightCentimeters(heightCentimeters);
        }

        //Set weight in kilograms
        if(spnWeightUnit.getSelectedItemPosition() == WeightUnits.LIBRAS) {
            float weightLibras = StringParser.stringToFloat(etWeight.getText().toString());
            float weightKilograms = WooserCalculator.calculateWeightKilograms(weightLibras);
            user.setWeightKg(weightKilograms);

        } else {
            float weightKilograms = StringParser.stringToFloat(etWeight.getText().toString());
            user.setWeightKg(weightKilograms);
        }

        mListener.onSaveUser(user);
    }

    protected boolean areFieldsValid() {

        boolean validName = !hasFieldError(etName);
        boolean validHeight = !hasFieldError(etHeight);
        boolean validWeight = !hasFieldError(etWeight);
        boolean validBirthday = !hasFieldError(etBirthday);

        return validName && validHeight && validWeight && validBirthday;
    }

    protected boolean hasFieldError(EditText et) {

        if(et.getText().toString().trim().isEmpty()) {
            et.setError(getString(R.string.validation_empty_field));
            return true;
        }

        et.setError(null);

        return false;
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void showDialogWithOptions(FragmentManager fragmentManager, ArrayList<String> options) {
        SelectionDialog.newInstance(options).show(fragmentManager, null);
    }

    @Override
    public void setUserGender(@Gender.Def int genderDef, String gender) {
        mSelectedGender = genderDef;
        etGender.setText(gender);
    }

    @Override
    public void setUserBodyStructure(@BodyStructure.Def int bodyStructureDef, String bodyStructure) {
        mSelectedBodyStructure = bodyStructureDef;
        etBodyStructure.setText(bodyStructure);
    }

    @Override
    public void setUserLifeStyle(@LifeStyle.Def int lifeStyleDef, String lifeStyle) {
        mSelectedLifeStyle = lifeStyleDef;
        etLifeStyle.setText(lifeStyle);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

    public Listener getListener() {
        return mListener;
    }

    public Calendar getBirthday() {
        return mBirthday;
    }

    public void setBirthday(Calendar birthday) {
        this.mBirthday = birthday;
    }

    public int getSelectedGender() {
        return mSelectedGender;
    }

    public void setSelectedGender(int selectedGender) {
        this.mSelectedGender = selectedGender;
    }

    public int getSelectedBodyStructure() {
        return mSelectedBodyStructure;
    }

    public void setSelectedBodyStructure(int selectedBodyStructure) {
        this.mSelectedBodyStructure = selectedBodyStructure;
    }

    public int getSelectedLifeStyle() {
        return mSelectedLifeStyle;
    }

    public void setSelectedLifeStyle(int selectedLifeStyle) {
        this.mSelectedLifeStyle = selectedLifeStyle;
    }

    public EditText getEtName() {
        return etName;
    }

    public EditText getEtBirthday() {
        return etBirthday;
    }

    public EditText getEtWeight() {
        return etWeight;
    }

    public EditText getEtHeight() {
        return etHeight;
    }

    public EditText getEtGender() {
        return etGender;
    }

    public EditText getEtBodyStructure() {
        return etBodyStructure;
    }

    public EditText getEtLifeStyle() {
        return etLifeStyle;
    }

    public Spinner getSpnWeightUnit() {
        return spnWeightUnit;
    }

    public Spinner getSpnHeightUnit() {
        return spnHeightUnit;
    }
}
