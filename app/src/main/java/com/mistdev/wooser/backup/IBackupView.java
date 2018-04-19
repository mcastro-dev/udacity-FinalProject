package com.mistdev.wooser.backup;

import com.mistdev.mvc.view.ILoading;
import com.mistdev.mvc.view.IOptionsMenu;

/**
 * Created by mcastro on 11/04/17.
 */

interface IBackupView extends IOptionsMenu, ILoading {

    interface Listener {
        void onHomeSelected();
        void upload();
        void download();
    }

    void setListener(Listener listener);
    void showSuccess(String message);
    void showFailure(String message);
}
