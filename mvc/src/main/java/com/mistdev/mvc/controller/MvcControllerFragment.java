package com.mistdev.mvc.controller;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mcastro on 09/03/17.
 */

public class MvcControllerFragment extends Fragment implements IMvcControllerFragment {

    public MvcControllerFragment() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @LayoutRes int layout) {

        View view = inflater.inflate(layout, container, false);

        setupActionBar();
        setupView(view);

        return view;
    }

    @Override
    public void setupActionBar() {

    }

    @Override
    public void setupView(View view) {

    }
}
