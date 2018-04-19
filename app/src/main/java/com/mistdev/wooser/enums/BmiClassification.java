package com.mistdev.wooser.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 14/03/17.
 */

public class BmiClassification {
    public static final int UNDERWEIGHT = 0;
    public static final int NORMAL = 1;
    public static final int OVERWEIGHT = 2;
    public static final int OBESE1 = 3;
    public static final int OBESE2 = 4;
    public static final int OBESE3 = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE1, OBESE2, OBESE3 })
    public @interface Def { }
}