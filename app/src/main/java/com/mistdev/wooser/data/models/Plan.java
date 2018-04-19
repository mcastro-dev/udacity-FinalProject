package com.mistdev.wooser.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.mistdev.wooser.data.database.WooserContract.PlanEntry;

/**
 * Created by mcastro on 21/03/17.
 */

public class Plan extends Entity implements IParser<Plan>, Parcelable {

    private long userId;
    private long startDate;
    private float startWeight;
    private long goalDate;
    private float goalWeight;

    public Plan() {
    }

    public Plan(long userId, long startDate, float startWeight, long goalDate, float goalWeight) {
        this.userId = userId;
        this.startDate = startDate;
        this.startWeight = startWeight;
        this.goalDate = goalDate;
        this.goalWeight = goalWeight;
    }

    public Plan(long id, long userId, long startDate, float startWeight, long goalDate, float goalWeight) {
        super(id);
        this.userId = userId;
        this.startDate = startDate;
        this.startWeight = startWeight;
        this.goalDate = goalDate;
        this.goalWeight = goalWeight;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public float getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(float startWeight) {
        this.startWeight = startWeight;
    }

    public long getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(long goalDate) {
        this.goalDate = goalDate;
    }

    public float getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(float goalWeight) {
        this.goalWeight = goalWeight;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    /* PARSER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public String idAlias() {
        return PlanEntry._ID;
    }

    @Override
    public Plan fromCursor(Cursor cursor) {
        Plan plan = null;

        try {
            int INDEX_ID = cursor.getColumnIndex(PlanEntry._ID);
            int INDEX_USER_ID = cursor.getColumnIndex(PlanEntry.COLUMN_USER_ID);
            int INDEX_START_DATE = cursor.getColumnIndex(PlanEntry.COLUMN_START_DATE);
            int INDEX_START_WEIGHT = cursor.getColumnIndex(PlanEntry.COLUMN_START_WEIGHT);
            int INDEX_GOAL_DATE = cursor.getColumnIndex(PlanEntry.COLUMN_GOAL_DATE);
            int INDEX_GOAL_WEIGHT = cursor.getColumnIndex(PlanEntry.COLUMN_GOAL_WEIGHT);

            long id = cursor.getLong(INDEX_ID);
            long userId = cursor.getLong(INDEX_USER_ID);
            long startDate = cursor.getLong(INDEX_START_DATE);
            float startWeight = cursor.getFloat(INDEX_START_WEIGHT);
            long goalDate = cursor.getLong(INDEX_GOAL_DATE);
            float goalWeight = cursor.getFloat(INDEX_GOAL_WEIGHT);

            plan = new Plan(id, userId, startDate, startWeight, goalDate, goalWeight);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return plan;
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(PlanEntry.COLUMN_USER_ID, this.getUserId());
        cv.put(PlanEntry.COLUMN_START_DATE, this.getStartDate());
        cv.put(PlanEntry.COLUMN_START_WEIGHT, this.getStartWeight());
        cv.put(PlanEntry.COLUMN_GOAL_DATE, this.getGoalDate());
        cv.put(PlanEntry.COLUMN_GOAL_WEIGHT, this.getGoalWeight());

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
        dest.writeLong(this.startDate);
        dest.writeFloat(this.startWeight);
        dest.writeLong(this.goalDate);
        dest.writeFloat(this.goalWeight);
    }

    protected Plan(Parcel in) {
        setId(in.readLong());
        this.userId = in.readLong();
        this.startDate = in.readLong();
        this.startWeight = in.readFloat();
        this.goalDate = in.readLong();
        this.goalWeight = in.readFloat();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel source) {
            return new Plan(source);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
}
