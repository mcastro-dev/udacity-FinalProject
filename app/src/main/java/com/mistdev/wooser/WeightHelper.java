package com.mistdev.wooser;

import android.content.Context;

import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.WeightUnits;

/**
 * Created by mcastro on 01/04/17.
 */

public class WeightHelper {

    public static String formatWeightText(Context context, @WeightUnits.Def int weightUnit, float weightKg) {

        switch (weightUnit) {

            case WeightUnits.KILOGRAMS:
                return String.format(
                        context.getString(R.string.format_weight_kg),
                        StringParser.floatToString(weightKg, WooserCalculator.WEIGHT_DEFAULT_DECIMAL_PLACES)
                );

            case WeightUnits.LIBRAS:
                float weightLb = WooserCalculator.calculatedWeightLibras(weightKg);
                return String.format(
                        context.getString(R.string.format_weight_lb),
                        StringParser.floatToString(weightLb, WooserCalculator.WEIGHT_DEFAULT_DECIMAL_PLACES)
                );

            default:
                throw new RuntimeException("WeightUnit not found");
        }
    }

    public static float convertKilogramsToLoggedUserWeightUnit(float weightKg) {

        User loggedUser = WooserCredentialManager.getInstance().getLoggedUser();
        @WeightUnits.Def int weightUnit = loggedUser.getWeightUnit();

        switch (weightUnit) {

            case WeightUnits.KILOGRAMS:
                return weightKg;

            case WeightUnits.LIBRAS:
                return WooserCalculator.calculatedWeightLibras(weightKg);

            default:
                throw new RuntimeException("WeightUnit not found");

        }
    }

    public static float convertLoggedUserWeightUnitToKilograms(float weight) {

        User loggedUser = WooserCredentialManager.getInstance().getLoggedUser();
        @WeightUnits.Def int weightUnit = loggedUser.getWeightUnit();

        switch (weightUnit) {

            case WeightUnits.KILOGRAMS:
                return weight;

            case WeightUnits.LIBRAS:
                return WooserCalculator.calculateWeightKilograms(weight);

            default:
                throw new RuntimeException("WeightUnit not found");

        }
    }

    public static float convertCentimetersToLoggedUserHeightUnit(float heightCm) {

        User loggedUser = WooserCredentialManager.getInstance().getLoggedUser();
        @HeightUnits.Def int heightUnit = loggedUser.getWeightUnit();

        switch (heightUnit) {

            case HeightUnits.CENTIMETERS:
                return heightCm;

            case HeightUnits.FEET:
                return WooserCalculator.calculateHeightFeet(heightCm);

            default:
                throw new RuntimeException("HeightUnit not found");

        }
    }

    public static float convertLoggedUserHeightUnitToCentimeters(float height) {

        User loggedUser = WooserCredentialManager.getInstance().getLoggedUser();
        @HeightUnits.Def int heightUnit = loggedUser.getWeightUnit();

        switch (heightUnit) {

            case HeightUnits.CENTIMETERS:
                return height;

            case HeightUnits.FEET:
                return WooserCalculator.calculateHeightCentimeters(height);

            default:
                throw new RuntimeException("HeightUnit not found");

        }
    }

}
