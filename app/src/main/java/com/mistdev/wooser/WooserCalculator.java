package com.mistdev.wooser;

import com.mistdev.wooser.enums.BmiClassification;

/**
 * Created by mcastro on 11/03/17.
 */

public class WooserCalculator {

    public static int WEIGHT_DEFAULT_DECIMAL_PLACES = 1;
    public static int HEIGHT_DEFAULT_DECIMAL_PLACES = 1;
    public static int BMI_DEFAULT_DECIMAL_PLACES = 1;

    private static float CM_TO_FEET = 0.0328084f;
    private static float KG_TO_LIBRA = 2.20462f;

    public static float calculateBMI(float weight, float heightCentimeters) {

        float metersHeight = heightCentimeters/100;
        return weight / (metersHeight * metersHeight);
    }

    public static @BmiClassification.Def int calculateBmiClassification(float bmi) {

        if(bmi < 18.5f) {
            return BmiClassification.UNDERWEIGHT;

        } else if(bmi >= 18.5f && bmi < 25f) {
            return BmiClassification.NORMAL;

        } else if(bmi >= 25f && bmi < 30f) {
            return BmiClassification.OVERWEIGHT;

        } else if(bmi >= 30f && bmi < 35f) {
            return BmiClassification.OBESE1;

        } else if(bmi >= 35f && bmi < 40f) {
            return BmiClassification.OBESE2;

        } else { //bmi >= 40f
            return BmiClassification.OBESE3;
        }
    }

    public static float calculateHeightFeet(float heightCentimeters) {
        return heightCentimeters * CM_TO_FEET;
    }

    public static float calculateHeightCentimeters(float heightFeet) {
        return heightFeet / CM_TO_FEET;
    }

    public static float calculatedWeightLibras(float weightKilograms) {
        return weightKilograms * KG_TO_LIBRA;
    }

    public static float calculateWeightKilograms(float weightLibras) {
        return weightLibras / KG_TO_LIBRA;
    }

}
