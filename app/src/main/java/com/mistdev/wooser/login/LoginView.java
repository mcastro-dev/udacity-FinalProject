package com.mistdev.wooser.login;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.github.clans.fab.FloatingActionButton;
import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;

/**
 * Created by mcastro on 04/03/17.
 */

class LoginView extends MvcView implements ILoginView {

    private Listener mListener;
    private View mEmptyView;

    LoginView(Context context, View root, LoginRecyclerAdapter adapter) {
        super(context, root);

        mEmptyView = getRootView().findViewById(R.id.empty_users_list);
        setupUsersRecyclerView(adapter);
        setupCreateUserFab();
        setupGoogleDriveFab();
    }

    private void setupUsersRecyclerView(LoginRecyclerAdapter adapter) {

        RecyclerView rcyUsers = (RecyclerView) getRootView().findViewById(R.id.rcy_users);
        rcyUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rcyUsers.setAdapter(adapter);
    }

    private void setupCreateUserFab() {

        FloatingActionButton fabCreateUser = (FloatingActionButton) getRootView().findViewById(R.id.fab_create_user);
        fabCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateUserClicked();
            }
        });
    }

    private void setupGoogleDriveFab() {

        FloatingActionButton fabGoogleDrive = (FloatingActionButton) getRootView().findViewById(R.id.fab_google_drive);
        fabGoogleDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mListener.onBackupClicked();
            }
        });
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void showEmptyListView() {

        if(mEmptyView.getVisibility() == View.GONE) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListView() {

        if(mEmptyView.getVisibility() == View.VISIBLE) {
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
