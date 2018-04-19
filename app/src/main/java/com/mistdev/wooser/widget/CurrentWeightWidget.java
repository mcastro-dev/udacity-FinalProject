package com.mistdev.wooser.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WeightHelper;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.enums.WeightUnits;
import com.mistdev.wooser.login.LoginActivity;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class CurrentWeightWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if(action.equals(context.getString(R.string.broadcast_logged_user_changed))) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, CurrentWeightWidget.class));

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {

        try {

            WooserCredentialManager manager = WooserCredentialManager.initialize(context);
            manager.initializeLoggedUser(context, new WooserCredentialManager.WooserCredentialListener() {
                @Override
                public void gotLoggedUser(User user) {

                    RemoteViews views;

                    if(user != null) {

                        views = new RemoteViews(context.getPackageName(), R.layout.widget_current_weight);

                        @WeightUnits.Def int weightUnit = user.getWeightUnit();
                        String weightText = WeightHelper.formatWeightText(context, weightUnit, user.getWeightKg());
                        views.setTextViewText(R.id.txt_weight, weightText);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(user.getWeightEntry().getDate());
                        String dateText = DateParser.calendarToLocalizedDateTimeString(calendar);
                        views.setTextViewText(R.id.txt_date, dateText);

                    } else {
                        views = new RemoteViews(context.getPackageName(), R.layout.widget_current_weight_empty);
                    }

                    setupOpenLoginIntent(context, views);

                    //Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupOpenLoginIntent(Context context, RemoteViews views) {

        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.btn_open_app, pendingIntent);
    }

}

