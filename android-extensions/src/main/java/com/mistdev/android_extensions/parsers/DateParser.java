package com.mistdev.android_extensions.parsers;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mcastro on 06/03/17.
 */

public class DateParser {

    public static String calendarToLocalizedString(Calendar calendar, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String calendarToLocalizedDateString(Calendar calendar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String calendarToLocalizedMonthString(Calendar calendar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String calendarToLocalizedTimeString(Calendar calendar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String calendarToLocalizedDateTimeString(Calendar calendar) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


}
