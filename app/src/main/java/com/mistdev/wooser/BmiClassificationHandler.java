package com.mistdev.wooser;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.mistdev.wooser.enums.BmiClassification;

/**
 * Created by mcastro on 14/03/17.
 */

public class BmiClassificationHandler {

    public static void setupBmiClassificationTextView(Context context, TextView txtBmiClassification, float bmi) {

        String classificationString = "";
        @ColorRes int classificationColor = R.color.color_gray;

        @BmiClassification.Def int bmiClassification = WooserCalculator.calculateBmiClassification(bmi);

        switch (bmiClassification) {

            case BmiClassification.UNDERWEIGHT:
                classificationString = context.getString(R.string.underweight);
                classificationColor = R.color.color_orange;
                break;

            case BmiClassification.NORMAL:
                classificationString = context.getString(R.string.normal);
                classificationColor = R.color.color_green;
                break;

            case BmiClassification.OVERWEIGHT:
                classificationString = context.getString(R.string.overweight);
                classificationColor = R.color.color_orange;
                break;

            case BmiClassification.OBESE1:
                classificationString = context.getString(R.string.obese_1);
                classificationColor = R.color.color_red;
                break;

            case BmiClassification.OBESE2:
                classificationString = context.getString(R.string.obese_2);
                classificationColor = R.color.color_red;
                break;

            case BmiClassification.OBESE3:
                classificationString = context.getString(R.string.obese_3);
                classificationColor = R.color.color_red;
                break;
        }

        txtBmiClassification.setText(classificationString);
        txtBmiClassification.setTextColor(ContextCompat.getColor(context, classificationColor));
    }

}
