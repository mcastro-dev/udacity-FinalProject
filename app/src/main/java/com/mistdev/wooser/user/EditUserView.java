package com.mistdev.wooser.user;

import android.content.Context;
import android.view.View;

import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.enums.BodyStructure;
import com.mistdev.wooser.enums.Gender;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.LifeStyle;

import java.util.Calendar;

/**
 * Created by mcastro on 03/04/17.
 */

class EditUserView extends UserView {

    EditUserView(Context context, View root, User editingUser) {
        super(context, root);

        setupViews(editingUser);
    }

    private void setupViews(User user) {

        //GONE
        getRootView().findViewById(R.id.textinput_user_weight).setVisibility(View.GONE);
        getRootView().findViewById(R.id.layout_units).setVisibility(View.GONE);

        //Name
        getEtName().setText(user.getName());

        //Weight Unit
        getSpnWeightUnit().setSelection(user.getWeightUnit());

        //Height Unit
        getSpnHeightUnit().setSelection(user.getHeightUnit());

        setupEditHeight(user);
        setupEditGender(user);
        setupEditBodyStructure(user);
        setupEditLifeStyle(user);
        setupEditBirthday(user);
    }

    private void setupEditHeight(User user) {

        String heightText = "";

        switch (user.getHeightUnit()) {

            case HeightUnits.CENTIMETERS:
                heightText = StringParser.floatToString(user.getHeightCentimeters(), WooserCalculator.HEIGHT_DEFAULT_DECIMAL_PLACES);
                break;

            case HeightUnits.FEET:
                float height = WooserCalculator.calculateHeightFeet(user.getHeightCentimeters());
                heightText = StringParser.floatToString(height, WooserCalculator.HEIGHT_DEFAULT_DECIMAL_PLACES);
                break;
        }

        getEtHeight().setText(heightText);
    }

    private void setupEditGender(User user) {

        @Gender.Def int gender = user.getGender();
        setSelectedGender(gender);
        getEtGender().setText(Gender.toString(getContext(), gender));
    }

    private void setupEditBodyStructure(User user) {

        @BodyStructure.Def int bodyStructure = user.getBodyStructure();
        setSelectedBodyStructure(bodyStructure);
        getEtBodyStructure().setText(BodyStructure.toString(getContext(), bodyStructure));
    }

    private void setupEditLifeStyle(User user) {

        @LifeStyle.Def int lifeStyle = user.getLifeStyle();
        setSelectedLifeStyle(lifeStyle);
        getEtLifeStyle().setText(LifeStyle.toString(getContext(), lifeStyle));
    }

    private void setupEditBirthday(User user) {

        Calendar birthday = getBirthday();
        birthday.setTimeInMillis(user.getBirthday());
        setBirthday(birthday);
        getEtBirthday().setText(DateParser.calendarToLocalizedDateString(birthday));
        setupBirthday();
    }

    @Override
    protected boolean areFieldsValid() {

        boolean validName = !hasFieldError(getEtName());
        boolean validHeight = !hasFieldError(getEtHeight());
        boolean validBirthday = !hasFieldError(getEtBirthday());

        return validName && validHeight && validBirthday;
    }
}
