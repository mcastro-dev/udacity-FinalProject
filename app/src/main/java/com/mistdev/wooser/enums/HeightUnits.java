package com.mistdev.wooser.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 04/03/17.
 */

public class HeightUnits {
    public static final int CENTIMETERS = 0;
    public static final int FEET = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ CENTIMETERS, FEET })
    public @interface Def { }
}
