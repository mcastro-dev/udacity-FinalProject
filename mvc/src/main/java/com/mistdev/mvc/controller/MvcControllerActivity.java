package com.mistdev.mvc.controller;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mcastro on 06/03/17.
 */

public abstract class MvcControllerActivity extends AppCompatActivity implements IMvcControllerActivity {

    public void onCreate(@LayoutRes int layout) {
        setContentView(layout);

        setupView();
        setupActionBar();
    }

    @Override
    public void setupActionBar() {

    }

    @Override
    public void setupView() {

    }
}
