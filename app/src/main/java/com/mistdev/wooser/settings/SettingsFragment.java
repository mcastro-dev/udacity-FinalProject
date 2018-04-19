package com.mistdev.wooser.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mistdev.mvc.controller.MvcControllerFragment;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.WooserIntentsHandler;
import com.mistdev.wooser.data.crud.CRUD;
import com.mistdev.wooser.data.crud.CRUDUser;
import com.mistdev.wooser.data.models.User;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.WeightUnits;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends MvcControllerFragment implements ISettingsView.Listener {

    private ISettingsView iView;
    private WooserCredentialManager mCredentialManager = WooserCredentialManager.getInstance();
    private SettingsListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {

        if(context instanceof SettingsListener) {
            mListener = (SettingsListener)context;
            super.onAttach(context);
            return;
        }
        throw new RuntimeException(context.getClass().getSimpleName() + " must implement " + SettingsListener.class.getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return onCreateView(inflater, container, R.layout.fragment_settings);
    }

    @Override
    public void setupView(View view) {
        super.setupView(view);

        iView = new SettingsView(getContext(), view);
        iView.setListener(this);
    }

    @Override
    public void updateWeightUnit(@WeightUnits.Def int weightUnit) {

        User loggedUser = mCredentialManager.getLoggedUser();
        loggedUser.setWeightUnit(weightUnit);

        updateUser(loggedUser);
    }

    @Override
    public void updateHeightUnit(@HeightUnits.Def int heightUnit) {

        User loggedUser = mCredentialManager.getLoggedUser();
        loggedUser.setHeightUnit(heightUnit);

        updateUser(loggedUser);
    }

    @Override
    public void onEditUser() {

        User user = mCredentialManager.getLoggedUser();
        WooserIntentsHandler.openUserActivity(getActivity(), user);
    }

    @Override
    public void onDeleteUser() {

        User user = mCredentialManager.getLoggedUser();

        try {
            CRUDUser crudUser = new CRUDUser(getContext());
            crudUser.delete(user.getId(), new CRUD.DeleteCallback() {
                @Override
                public void onDeleteSuccess(int deleteCount) {
                    mListener.onLoggedUserDeleted();
                }

                @Override
                public void onDeleteFail() {
                    Toast.makeText(getContext(), getString(R.string.error_delete_user), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser(final User user) {

        try {
            CRUDUser crudUser = new CRUDUser(getContext());
            crudUser.update(user.getId(), user, new CRUD.UpdateCallback() {
                @Override
                public void onUpdateSuccess(int updateCount) {

                    mCredentialManager.updateLoggedUser(getContext(), user);
                }

                @Override
                public void onUpdateFail() {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
