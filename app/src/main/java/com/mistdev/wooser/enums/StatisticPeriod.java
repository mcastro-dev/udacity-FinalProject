package com.mistdev.wooser.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 27/03/17.
 */

public class StatisticPeriod {
    public static final int ONE_WEEK = 0;
    public static final int ONE_MONTH = 1;
    public static final int SIX_MONTHS = 2;
    public static final int ONE_YEAR = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ ONE_WEEK, ONE_MONTH, SIX_MONTHS, ONE_YEAR })
    public @interface Def { }
}