package com.mistdev.mvc.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.view.View;

/**
 * Created by mcastro on 02/02/17.
 */

public abstract class MvcView implements IMvcView {

    private Context mContext;
    private View mRootView;

    public MvcView(Context context, View root) {
        mContext = context;
        mRootView = root;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void configureActionBar(ActionBar actionBar) {
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }
}
