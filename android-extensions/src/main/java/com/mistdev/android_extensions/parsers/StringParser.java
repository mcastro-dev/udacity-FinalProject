package com.mistdev.android_extensions.parsers;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by mcastro on 06/03/17.
 */

public class StringParser {

    /**
     * Parses strings to floats.
     * If NullPointerException or NumberFormatException, returns 0.
     * @param numberString the string to be parsed
     * @return parsed
     */
    public static float stringToFloat(String numberString) {

        float result = 0;

        try {
            result = Float.parseFloat(numberString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Parses strings to integers.
     * If NullPointerException or NumberFormatException, returns 0.
     * @param numberString the string to be parsed
     * @return parsed
     */
    public static int stringToInt(String numberString) {

        int result = 0;

        try {
            result = Integer.parseInt(numberString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String floatToString(float value, int decimalPlaces) {

        String format = "#0";

        for(int i = 0; i < decimalPlaces; i++) {

            if(i == 0) {
                format += ".0";

            } else {
                format += "0";
            }
        }
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

}
