package com.mistdev.wooser.createEntry;

import android.os.Bundle;
import android.view.MenuItem;

import com.mistdev.mvc.controller.MvcControllerActivity;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserAnalytics;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.crud.CRUDWeightEntry;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.data.models.WeightEntry;

public class CreateEntryActivity extends MvcControllerActivity implements ICreateEntryView.Listener {

    private static final String ANALYTICS_CATEGORY = "Weight Entry";

    private ICreateEntryView iView;
    private WooserAnalytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_create_entry);

        mAnalytics = new WooserAnalytics(this);
    }

    @Override
    public void setupView() {
        super.setupView();

        iView = new CreateEntryView(this, findViewById(android.R.id.content));
        iView.setListener(this);
        iView.setupActionBar(getSupportActionBar());
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
    public void onCreateEntryClicked(final WeightEntry entry) {

        try {

            CRUDWeightEntry crudWeightEntry = new CRUDWeightEntry(this);
            crudWeightEntry.create(entry, new CRUD.CreateCallback() {
                @Override
                public void onCreateSuccess(long objId) {

                    mAnalytics.sendCreated(ANALYTICS_CATEGORY, "onCreateSuccess()", objId);
                    entry.setId(objId);
                    onCreateEntrySuccess(entry);
                }

                @Override
                public void onCreateFail() {
                    iView.showError(getString(R.string.error_create_weight_entry));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCreateEntrySuccess(WeightEntry weightEntry) {

        WooserCredentialManager credentialManager = WooserCredentialManager.getInstance();
        User loggedUser = credentialManager.getLoggedUser();

        //Only update the user current weight if the new entry is newer
        if(loggedUser.getWeightEntry().getDate() <= weightEntry.getDate()) {

            loggedUser.setWeightEntry(weightEntry);
            credentialManager.updateLoggedUser(this, loggedUser);
        }
        finish();
    }
}
