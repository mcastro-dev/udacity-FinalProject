package com.mistdev.wooser.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 04/03/17.
 */

public class WeightUnits {
    public static final int KILOGRAMS = 0;
    public static final int LIBRAS = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ KILOGRAMS, LIBRAS })
    public @interface Def { }
}
