package com.mistdev.wooser.enums;

import android.content.Context;
import android.support.annotation.IntDef;

import com.mistdev.wooser.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mcastro on 04/03/17.
 */

public class Gender {
    public static final int NONE = -1;
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ MALE, FEMALE })
    public @interface Def { }

    public static String toString(Context context, @Gender.Def int gender) {
        String[] gendersArray = context.getResources().getStringArray(R.array.genders);
        return gendersArray[gender];
    }
}
