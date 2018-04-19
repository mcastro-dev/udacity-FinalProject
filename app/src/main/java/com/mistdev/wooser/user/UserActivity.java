package com.mistdev.wooser.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserAnalytics;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.crud.CRUDUser;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.dialogs.SelectionDialogListener;

import java.util.ArrayList;
import java.util.Arrays;

public class UserActivity extends MvcControllerActivity implements
        IUserView.Listener,
        SelectionDialogListener {

    public static final String ARG_USER = "ARG_USER";
    private static final String ANALYTICS_CATEGORY = "User";

    private static final int DIALOG_NONE = -1;
    private static final int DIALOG_GENDER = 0;
    private static final int DIALOG_BODY_STRUCTURE = 1;
    private static final int DIALOG_LIFE_STYLE = 2;

    private IUserView iView;
    private WooserAnalytics mAnalytics;
    private User mEditingUser;
    private int mClickedDialog = DIALOG_NONE;
    private ArrayList<String> mGenders;
    private ArrayList<String> mBodyStructures;
    private ArrayList<String> mLifeStyles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnalytics = new WooserAnalytics(this);

        handleReceivedIntent();

        onCreate(R.layout.activity_user);

        setupGenders();
        setupBodyStructures();
        setupLifeStyles();
    }

    private void handleReceivedIntent() {

        Intent intent = getIntent();
        if(intent == null || !intent.hasExtra(ARG_USER)) {
            return;
        }

        mEditingUser = intent.getParcelableExtra(ARG_USER);
    }

    @Override
    public void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            if(isEditingUser()) {
                actionBar.setTitle(getString(R.string.view_title_edit_user));

            } else {
                actionBar.setTitle(getString(R.string.view_title_create_user));
            }
        }
    }

    @Override
    public void setupView() {

        if(isEditingUser()) {
            iView = new EditUserView(this, findViewById(android.R.id.content), mEditingUser);

        } else {
            iView = new CreateUserView(this, findViewById(android.R.id.content));
        }
        iView.setListener(this);
    }

    private void setupGenders() {
        String[] gendersArray = getResources().getStringArray(R.array.genders);
        mGenders = new ArrayList<>(Arrays.asList(gendersArray));
    }

    private void setupBodyStructures() {
        String[] bodysArray = getResources().getStringArray(R.array.body_structures);
        mBodyStructures = new ArrayList<>(Arrays.asList(bodysArray));
    }

    private void setupLifeStyles() {
        String[] stylesArray = getResources().getStringArray(R.array.life_styles);
        mLifeStyles = new ArrayList<>(Arrays.asList(stylesArray));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return iView.onOptionsItemSelected(item);
    }

    @Override
    public void onHomeSelected() {
        finish();
    }

    @Override
    public void onSaveUser(User user) {

        if(isEditingUser()) {
            editUser(user);
            return;
        }

        createUser(user);
    }

    @Override
    public void onDialogOptionSelected(int position) {

        switch (mClickedDialog) {
            case DIALOG_GENDER:
                iView.setUserGender(position, mGenders.get(position));
                break;

            case DIALOG_BODY_STRUCTURE:
                iView.setUserBodyStructure(position, mBodyStructures.get(position));
                break;

            case DIALOG_LIFE_STYLE:
                iView.setUserLifeStyle(position, mLifeStyles.get(position));
                break;
        }

        mClickedDialog = DIALOG_NONE;
    }

    @Override
    public void genderClicked() {
        setDialogAndClearFocus(DIALOG_GENDER);
        iView.showDialogWithOptions(getSupportFragmentManager(), mGenders);
    }

    @Override
    public void bodyStructureClicked() {
        setDialogAndClearFocus(DIALOG_BODY_STRUCTURE);
        iView.showDialogWithOptions(getSupportFragmentManager(), mBodyStructures);
    }

    @Override
    public void lifeStyleClicked() {
        setDialogAndClearFocus(DIALOG_LIFE_STYLE);
        iView.showDialogWithOptions(getSupportFragmentManager(), mLifeStyles);
    }

    private boolean isEditingUser() {
        return (mEditingUser != null);
    }

    private void createUser(User user) {

        iView.showLoading();

        CRUDUser crudUser = new CRUDUser(this);

        try {
            crudUser.createUserAndWeightEntry(user, new CRUD.CreateCallback() {
                @Override
                public void onCreateSuccess(long objId) {

                    mAnalytics.sendCreated(ANALYTICS_CATEGORY, "onCreateSuccess()", objId);
                    iView.hideLoading();
                    finish();
                }

                @Override
                public void onCreateFail() {
                    iView.hideLoading();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editUser(User user) {

        mEditingUser.setName(user.getName());
        mEditingUser.setHeightCentimeters(user.getHeightCentimeters());
        mEditingUser.setBirthday(user.getBirthday());
        mEditingUser.setGender(user.getGender());
        mEditingUser.setBodyStructure(user.getBodyStructure());
        mEditingUser.setLifeStyle(user.getLifeStyle());

        iView.showLoading();

        CRUDUser crudUser = new CRUDUser(this);

        try {
            crudUser.update(mEditingUser.getId(), mEditingUser, new CRUD.UpdateCallback() {
                @Override
                public void onUpdateSuccess(int updateCount) {

                    WooserCredentialManager.getInstance().updateLoggedUser(UserActivity.this, mEditingUser);

                    mAnalytics.sendCreated(ANALYTICS_CATEGORY, "onUpdateSuccess()", updateCount);

                    iView.hideLoading();
                    finish();
                }

                @Override
                public void onUpdateFail() {
                    iView.hideLoading();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDialogAndClearFocus(int clickedDialog) {
        mClickedDialog = clickedDialog;
        clearFocus();
    }

    private void clearFocus() {
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            focusedView.clearFocus();
        }
    }
}
