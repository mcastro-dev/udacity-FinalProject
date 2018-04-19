package com.mistdev.wooser.enums;

import android.content.Context;
import android.support.annotation.IntDef;

import com.mistdev.wooser.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 04/03/17.
 */

public class BodyStructure {
    public static final int NONE = -1;
    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ SMALL, MEDIUM, LARGE })
    public @interface Def { }

    public static String toString(Context context, @BodyStructure.Def int bodyStructure) {
        String[] bodysArray = context.getResources().getStringArray(R.array.body_structures);
        return bodysArray[bodyStructure];
    }
}
