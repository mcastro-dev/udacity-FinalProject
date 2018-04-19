package com.mistdev.wooser.login;

/**
 * Created by mcastro on 04/03/17.
 */

interface ILoginView {

    interface Listener {
        void onCreateUserClicked();
        void onBackupClicked();
    }

    void setListener(Listener listener);
    void showEmptyListView();
    void hideEmptyListView();
}
