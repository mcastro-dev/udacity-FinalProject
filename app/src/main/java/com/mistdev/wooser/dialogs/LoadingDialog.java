package com.mistdev.wooser.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mistdev.wooser.R;

/**
 * Created by mcastro on 12/04/17.
 */

public class LoadingDialog extends DialogFragment {

    private static final String TAG = "tag_loading_dialog";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_loading, container, false);
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

}
