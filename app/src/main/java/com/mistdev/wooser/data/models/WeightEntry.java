package com.mistdev.wooser.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.mistdev.wooser.data.database.WooserContract.WeightEntryEntry;

/**
 * Created by mcastro on 09/03/17.
 */

public class WeightEntry extends Entity implements IParser<WeightEntry>, Parcelable {

    private long userId;
    private long date;
    private float weightKg;
    private float bmi;

    public WeightEntry() {
    }

    public WeightEntry(long userId, long date, float weightKg, float bmi) {
        this.userId = userId;
        this.date = date;
        this.weightKg = weightKg;
        this.bmi = bmi;
    }

    public WeightEntry(long id, long userId, long date, float weightKg, float bmi) {
        super(id);
        this.userId = userId;
        this.date = date;
        this.weightKg = weightKg;
        this.bmi = bmi;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(float weightKg) {
        this.weightKg = weightKg;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }


    /* PARSER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public String idAlias() {
        return WeightEntryEntry._ID;
    }

    @Override
    public WeightEntry fromCursor(Cursor cursor) {
        WeightEntry weightEntry = null;

        try {
            int INDEX_ID = cursor.getColumnIndex(WeightEntryEntry._ID);
            int INDEX_USER_ID = cursor.getColumnIndex(WeightEntryEntry.COLUMN_USER_ID);
            int INDEX_DATE = cursor.getColumnIndex(WeightEntryEntry.COLUMN_DATE);
            int INDEX_WEIGHT = cursor.getColumnIndex(WeightEntryEntry.COLUMN_WEIGHT_KG);
            int INDEX_BMI = cursor.getColumnIndex(WeightEntryEntry.COLUMN_BMI);

            long id = cursor.getLong(INDEX_ID);
            long userId = cursor.getLong(INDEX_USER_ID);
            long date = cursor.getLong(INDEX_DATE);
            float weight = cursor.getFloat(INDEX_WEIGHT);
            float bmi = cursor.getFloat(INDEX_BMI);

            weightEntry = new WeightEntry();
            weightEntry.setId(id);
            weightEntry.setUserId(userId);
            weightEntry.setDate(date);
            weightEntry.setWeightKg(weight);
            weightEntry.setBmi(bmi);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return weightEntry;
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(WeightEntryEntry.COLUMN_USER_ID, this.getUserId());
        cv.put(WeightEntryEntry.COLUMN_DATE, this.getDate());
        cv.put(WeightEntryEntry.COLUMN_WEIGHT_KG, this.getWeightKg());
        cv.put(WeightEntryEntry.COLUMN_BMI, this.getBmi());

        return cv;
    }


    /* PARCELABLE
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(this.userId);
        dest.writeLong(this.date);
        dest.writeFloat(this.weightKg);
        dest.writeFloat(this.bmi);
    }

    protected WeightEntry(Parcel in) {
        setId(in.readLong());
        this.userId = in.readLong();
        this.date = in.readLong();
        this.weightKg = in.readFloat();
        this.bmi = in.readFloat();
    }

    public static final Creator<WeightEntry> CREATOR = new Creator<WeightEntry>() {
        @Override
        public WeightEntry createFromParcel(Parcel source) {
            return new WeightEntry(source);
        }

        @Override
        public WeightEntry[] newArray(int size) {
            return new WeightEntry[size];
        }
    };
}
