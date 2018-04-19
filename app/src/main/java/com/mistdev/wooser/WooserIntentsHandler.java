package com.mistdev.wooser;

import android.app.Activity;
import android.content.Intent;

import com.mistdev.wooser.backup.BackupActivity;
import com.mistdev.wooser.createEntry.CreateEntryActivity;
import com.mistdev.wooser.createPlan.CreatePlanActivity;
import com.mistdev.wooser.user.UserActivity;
import com.mistdev.wooser.data.models.Plan;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.login.LoginActivity;
import com.mistdev.wooser.main.MainActivity;
import com.mistdev.wooser.statistics.StatisticsActivity;

/**
 * Created by mcastro on 08/03/17.
 */

public class WooserIntentsHandler {

    public static void openMainActivity(Activity activity) {

        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void openLoginActivity(Activity activity) {

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void openUserActivity(Activity activity) {

        Intent intent = new Intent(activity, UserActivity.class);
        activity.startActivity(intent);
    }

    public static void openUserActivity(Activity activity, User user) {

        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(UserActivity.ARG_USER, user);
        activity.startActivity(intent);
    }

    public static void openCreateWeightEntryActivity(Activity activity) {

        Intent intent = new Intent(activity, CreateEntryActivity.class);
        activity.startActivity(intent);
    }

    public static void openCreatePlan(Activity activity) {

        Intent intent = new Intent(activity, CreatePlanActivity.class);
        activity.startActivity(intent);
    }

    public static void openCreatePlan(Activity activity, Plan plan) {

        Intent intent = new Intent(activity, CreatePlanActivity.class);
        intent.putExtra(CreatePlanActivity.ARG_PLAN, plan);
        activity.startActivity(intent);
    }

    public static void openStatistics(Activity activity) {

        Intent intent = new Intent(activity, StatisticsActivity.class);
        activity.startActivity(intent);
    }

    public static void openBackup(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, BackupActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

}
