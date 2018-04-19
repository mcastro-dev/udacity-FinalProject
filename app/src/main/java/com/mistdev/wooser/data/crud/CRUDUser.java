package com.mistdev.wooser.data.crud;

import android.content.Context;
import android.net.Uri;

import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.data.database.WooserContract;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.WeightUnits;

import java.util.Calendar;

/**
 * Created by mcastro on 08/03/17.
 */

public class CRUDUser extends CRUD<User> {

    private Context mContext;

    public CRUDUser(Context context) {
        super(context, WooserContract.UserEntry.CONTENT_URI, User.class);
        mContext = context;
    }

    public void createUserAndWeightEntry(final User obj, final CreateCallback callback) throws Exception {
        super.create(obj, new CreateCallback() {
            @Override
            public void onCreateSuccess(long objId) {

                createWeightEntry(objId, obj, callback);
            }

            @Override
            public void onCreateFail() {

            }
        });

    }

    private void createWeightEntry(long userId, final User user, final CreateCallback callback) {

        try {
            Calendar calendar = Calendar.getInstance();
            float bmi = WooserCalculator.calculateBMI(user.getWeightKg(), user.getHeightCentimeters());
            WeightEntry weightEntry = new WeightEntry(userId, calendar.getTimeInMillis(), user.getWeightKg(), bmi);

            CRUDWeightEntry crudWeightEntry = new CRUDWeightEntry(mContext);
            crudWeightEntry.create(weightEntry, callback);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onCreateFail();
        }
    }
}
