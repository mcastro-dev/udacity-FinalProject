package com.mistdev.wooser.enums;

import android.content.Context;
import android.support.annotation.IntDef;

import com.mistdev.wooser.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 04/03/17.
 */

public class LifeStyle {
    public static final int NONE = -1;
    public static final int SEDENTARY = 0;
    public static final int LIGHTLY_ACTIVE = 1;
    public static final int MODERATELY_ACTIVE = 2;
    public static final int VERY_ACTIVE = 3;
    public static final int EXTREMELY_ACTIVE = 4;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE, EXTREMELY_ACTIVE })
    public @interface Def { }

    public static String toString(Context context, @LifeStyle.Def int lifeStyle) {
        String[] stylesArray = context.getResources().getStringArray(R.array.life_styles);
        return stylesArray[lifeStyle];
    }
}
