package com.mistdev.wooser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.crud.CRUDUser;
import com.mistdev.wooser.data.models.User;

/**
 * Created by mcastro on 08/03/17.
 */

public class WooserCredentialManager {

    private static final String KEY_LOGGED_USER_ID = "logged_user_id";
    private static final long PREF_DEFAULT_NO_USER = -1;

    private static WooserCredentialManager instance;

    private SharedPreferences mSharedPreferences;
    private User mUser;

    synchronized public static WooserCredentialManager initialize(Context context) {

        if(instance == null) {
            instance = new WooserCredentialManager(context.getApplicationContext());
        }
        return instance;
    }

    synchronized public static WooserCredentialManager getInstance() {

        if(instance == null) {
            throw new RuntimeException(WooserCredentialManager.class.getSimpleName() + " must be initialized with Context first");
        }
        return instance;
    }

    private WooserCredentialManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    synchronized public void initializeLoggedUser(Context context, WooserCredentialListener listener) {

        if(listener != null && mUser != null) {
            listener.gotLoggedUser(mUser);
            return;
        }

        setupLoggedUser(context, listener);
    }

    public User getLoggedUser() {
        return mUser;
    }

    public void setLoggedUser(Context context, User user) {

        mUser = user;
        updateLoggedUserSharedPref(user.getId());
        broadcastLoggedUserChanged(context);
    }

    /**
     * IMPORTANT: Does not update the user ID! For that you must use setLoggedUser().
     */
    public void updateLoggedUser(Context context, User user) {

        user.setId(mUser.getId());
        mUser = user;
        broadcastLoggedUserChanged(context);
    }

    public void logoutUser(Context context) {

        mUser = null;
        updateLoggedUserSharedPref(PREF_DEFAULT_NO_USER);
        broadcastLoggedUserChanged(context);
    }

    private long getUserIdFromSharedPrefs() {
        return mSharedPreferences.getLong(KEY_LOGGED_USER_ID, PREF_DEFAULT_NO_USER);
    }

    private void updateLoggedUserSharedPref(long userId) {
        mSharedPreferences.edit().putLong(KEY_LOGGED_USER_ID, userId).apply();
    }

    private void setupLoggedUser(Context context, final WooserCredentialListener listener) {

        long loggedUserId = getUserIdFromSharedPrefs();

        if(loggedUserId == PREF_DEFAULT_NO_USER) {
            mUser = null;
            propagateLoggedUser(listener);
            return;
        }

        try {

            CRUDUser crudUser = new CRUDUser(context);
            crudUser.read(loggedUserId, new CRUD.ReadCallback<User>() {
                @Override
                public void onReadSuccess(User obj) {
                    mUser = obj;
                    propagateLoggedUser(listener);
                }

                @Override
                public void onReadFail() {
                    mUser = null;
                    propagateLoggedUser(listener);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void propagateLoggedUser(WooserCredentialListener listener) {
        if(listener != null) {
            listener.gotLoggedUser(mUser);
        }
    }

    private void broadcastLoggedUserChanged(Context context) {
        Intent intent = new Intent(context.getString(R.string.broadcast_logged_user_changed));
        context.sendBroadcast(intent);
    }

    public interface WooserCredentialListener {
        void gotLoggedUser(User user);
    }

}
