package com.mistdev.wooser.statistics;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mistdev.android_extensions.adapters.CursorRecyclerViewAdapter;
import com.mistdev.android_extensions.parsers.DateParser;
import com.mistdev.android_extensions.parsers.StringParser;
import com.mistdev.wooser.BmiClassificationHandler;
import com.mistdev.wooser.R;
import com.mistdev.wooser.WeightHelper;
import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.WooserCredentialManager;
import com.mistdev.wooser.data.models.WeightEntry;
import com.mistdev.wooser.enums.WeightUnits;

import java.util.Calendar;

/**
 * Created by mcastro on 28/03/17.
 */

class StatisticsRecyclerAdapter extends CursorRecyclerViewAdapter<StatisticsRecyclerAdapter.EntriesViewHolder> {

    private @WeightUnits.Def int weightUnit = WooserCredentialManager.getInstance().getLoggedUser().getWeightUnit();

    StatisticsRecyclerAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public StatisticsRecyclerAdapter.EntriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        return new StatisticsRecyclerAdapter.EntriesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_weight_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(StatisticsRecyclerAdapter.EntriesViewHolder holder, Cursor cursor) {

        if(cursor == null) {
            return;
        }

        WeightEntry weightEntry = new WeightEntry().fromCursor(cursor);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(weightEntry.getDate());

        Context context = holder.itemView.getContext();

        String dateText = DateParser.calendarToLocalizedDateTimeString(calendar);
        String weightText = WeightHelper.formatWeightText(context, weightUnit, weightEntry.getWeightKg());

        String bmiText = String.format(
                context.getString(R.string.format_bmi),
                StringParser.floatToString(weightEntry.getBmi(), WooserCalculator.BMI_DEFAULT_DECIMAL_PLACES)
        );
        String weightVariationText = getWeightVariationText(context, weightEntry, cursor);

        holder.txtDate.setText(dateText);
        holder.txtWeight.setText(weightText);
        holder.txtWeightVariation.setText(weightVariationText);
        holder.txtBmi.setText(bmiText);

        BmiClassificationHandler.setupBmiClassificationTextView(context, holder.txtBmiClassification, weightEntry.getBmi());
    }

    private String getWeightVariationText(Context context, WeightEntry weightEntry, Cursor cursor) {

        String weightVariationText = context.getString(R.string.weight_variation_not_available);

        WeightEntry nextWeightEntry = null;

        //Get next weight to calculate weight variation
        if(cursor.getCount() > 1 && !cursor.isLast()) {

            cursor.moveToNext();
            nextWeightEntry = new WeightEntry().fromCursor(cursor);
            cursor.moveToPrevious();
        }

        if(nextWeightEntry == null) {
            return weightVariationText;
        }

        float previousWeight = 0;
        float currentWeight = 0;

        if(weightEntry.getDate() > nextWeightEntry.getDate()) {
            previousWeight = nextWeightEntry.getWeightKg();
            currentWeight = weightEntry.getWeightKg();

        } else {
            previousWeight = weightEntry.getWeightKg();
            currentWeight = nextWeightEntry.getWeightKg();
        }

        float weightVariation = currentWeight - previousWeight;
        weightVariationText = WeightHelper.formatWeightText(context, weightUnit, weightVariation);

        return weightVariationText;
    }

    /* VIEW HOLDER
     * ----------------------------------------------------------------------------------------------*/
    class EntriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtWeight;
        TextView txtWeightVariation;
        TextView txtBmi;
        TextView txtBmiClassification;

        EntriesViewHolder(View itemView) {
            super(itemView);

            txtDate = (TextView)itemView.findViewById(R.id.txt_date);
            txtWeight = (TextView)itemView.findViewById(R.id.txt_weight);
            txtWeightVariation = (TextView)itemView.findViewById(R.id.txt_weight_variation);
            txtBmi = (TextView)itemView.findViewById(R.id.txt_bmi);
            txtBmiClassification = (TextView)itemView.findViewById(R.id.txt_bmi_classification);
        }
    }
}