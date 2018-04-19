package com.mistdev.wooser.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mistdev.wooser.R;

import java.util.ArrayList;

/**
 * Created by mcastro on 07/03/17.
 */

public class SelectionDialog extends DialogFragment implements SelectionDialogListener {

    public static final String ARG_OPTIONS = "arg_options";
    private ArrayList<String> mOptions;
    private SelectionDialogListener mListener;

    public static SelectionDialog newInstance(ArrayList<String> options) {

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_OPTIONS, options);

        SelectionDialog fragment = new SelectionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {

        if(context instanceof SelectionDialogListener) {
            mListener = (SelectionDialogListener)context;
            super.onAttach(context);
            return;
        }

        throw new RuntimeException(context.getClass().getSimpleName() + " must implement " + SelectionDialogListener.class.getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setStyle(STYLE_NO_TITLE, getTheme());

        mOptions = getArguments().getStringArrayList(ARG_OPTIONS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_selection, container, false);

        setupOptions(view);
        setupButtons(view);

        return view;
    }

    private void setupOptions(View view) {
        RecyclerView rcyOptions = (RecyclerView)view.findViewById(R.id.rcy_selection_options);
        SelectionDialogAdapter adapter = new SelectionDialogAdapter(mOptions, this);
        rcyOptions.setLayoutManager(new LinearLayoutManager(getContext()));
        rcyOptions.setAdapter(adapter);
    }

    private void setupButtons(View view) {
        Button btnCancel = (Button)view.findViewById(R.id.btn_selection_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDialogOptionSelected(int position) {
        mListener.onDialogOptionSelected(position);
        dismiss();
    }
}
