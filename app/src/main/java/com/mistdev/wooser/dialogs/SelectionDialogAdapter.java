package com.mistdev.wooser.dialogs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mistdev.wooser.R;

import java.util.List;

/**
 * Created by mcastro on 07/03/17.
 */

class SelectionDialogAdapter extends RecyclerView.Adapter<SelectionDialogAdapter.ViewHolder> {

    private List<String> mOptions;
    private SelectionDialogListener mListener;

    SelectionDialogAdapter(List<String> options, SelectionDialogListener listener) {
        mOptions = options;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_dialog_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(mOptions == null || mOptions.isEmpty()) {
            return;
        }

        holder.txtOption.setText(mOptions.get(position));

        final int itemPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogOptionSelected(itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mOptions != null) ? mOptions.size() : 0;
    }


    /* VIEW HOLDER
     * ----------------------------------------------------------------------------------------------*/
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtOption;

        ViewHolder(View itemView) {
            super(itemView);

            txtOption = (TextView)itemView.findViewById(R.id.txt_option);
        }
    }
}
