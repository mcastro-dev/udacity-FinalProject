package com.mistdev.wooser.backup;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mistdev.mvc.view.MvcView;
import com.mistdev.wooser.R;
import com.mistdev.wooser.dialogs.LoadingDialog;

/**
 * Created by mcastro on 11/04/17.
 */

class BackupView extends MvcView implements IBackupView {

    private Listener mListener;
    private LoadingDialog mLoading;
    private FragmentManager mFragmentManager;

    BackupView(Context context, View root, FragmentManager manager) {
        super(context, root);

        mFragmentManager = manager;
        setupViews();
    }

    private void setupViews() {

        mLoading = new LoadingDialog();

        Button btnUpload = (Button)getRootView().findViewById(R.id.btn_upload);
        Button btnDownload = (Button)getRootView().findViewById(R.id.btn_download);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadDialog();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadDialog();
            }
        });
    }

    private void showUploadDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.backup_upload_dialog_title))
                .setMessage(getString(R.string.backup_upload_dialog_message))
                .setPositiveButton(getString(R.string.backup_upload_dialog_confirmation_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.upload();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), null)
                .create()
                .show();
    }

    private void showDownloadDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.backup_download_dialog_title))
                .setMessage(getString(R.string.backup_download_dialog_message))
                .setPositiveButton(getString(R.string.backup_download_dialog_confirmation_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.download();
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), null)
                .create()
                .show();
    }

    @Override
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mListener.onHomeSelected();
                return true;
        }

        return false;
    }

    @Override
    public void showSuccess(String message) {
        showDialog(getString(R.string.success), message);
    }

    @Override
    public void showFailure(String message) {
        showDialog(getString(R.string.error), message);
    }

    private void showDialog(String title, String message) {

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(android.R.string.ok), null)
                .create()
                .show();
    }

    @Override
    public void showLoading() {
        mLoading.show(mFragmentManager);
    }

    @Override
    public void hideLoading() {
        mLoading.dismiss();
    }
}
