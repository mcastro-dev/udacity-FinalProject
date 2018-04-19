package com.mistdev.wooser.data.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.mistdev.wooser.WooserCalculator;
import com.mistdev.wooser.data.database.WooserContract;
import com.mistdev.wooser.data.database.WooserContract.UserEntry;
import com.mistdev.wooser.enums.BodyStructure;
import com.mistdev.wooser.enums.Gender;
import com.mistdev.wooser.enums.HeightUnits;
import com.mistdev.wooser.enums.LifeStyle;
import com.mistdev.wooser.enums.WeightUnits;

/**
 * Created by mcastro on 04/03/17.
 */

public class User extends Entity implements IParser<User>, Parcelable {

    private @WeightUnits.Def int weightUnit;
    private @HeightUnits.Def int heightUnit;
    private @Gender.Def int gender;
    private @BodyStructure.Def int bodyStructure;
    private @LifeStyle.Def int lifeStyle;
    private String name;
    private String email;
    private String avatarPath;
    private long birthday;
    private float heightCentimeters;
    private WeightEntry weightEntry;

    public User() {
    }

    public User(int weightUnit, int heightUnit, int gender, int bodyStructure, int lifeStyle, String name,
                String email, String avatarPath, long birthday, float heightCentimeters) {

        this.weightUnit = weightUnit;
        this.heightUnit = heightUnit;
        this.gender = gender;
        this.bodyStructure = bodyStructure;
        this.lifeStyle = lifeStyle;
        this.name = name;
        this.email = email;
        this.avatarPath = avatarPath;
        this.birthday = birthday;
        this.heightCentimeters = heightCentimeters;
    }

    public User(long id, int weightUnit, int heightUnit, int gender, int bodyStructure, int lifeStyle,
                String name, String email, String avatarPath, long birthday, float heightCentimeters) {

        super(id);
        this.weightUnit = weightUnit;
        this.heightUnit = heightUnit;
        this.gender = gender;
        this.bodyStructure = bodyStructure;
        this.lifeStyle = lifeStyle;
        this.name = name;
        this.email = email;
        this.avatarPath = avatarPath;
        this.birthday = birthday;
        this.heightCentimeters = heightCentimeters;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public int getHeightUnit() {
        return heightUnit;
    }

    public int getGender() {
        return gender;
    }

    public int getBodyStructure() {
        return bodyStructure;
    }

    public int getLifeStyle() {
        return lifeStyle;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public long getBirthday() {
        return birthday;
    }

    public float getHeightCentimeters() {
        return heightCentimeters;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public void setHeightUnit(int heightUnit) {
        this.heightUnit = heightUnit;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setBodyStructure(int bodyStructure) {
        this.bodyStructure = bodyStructure;
    }

    public void setLifeStyle(int lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setHeightCentimeters(float heightCentimeters) {
        this.heightCentimeters = heightCentimeters;
    }

    public WeightEntry getWeightEntry() {
        return weightEntry;
    }

    public void setWeightEntry(WeightEntry weightEntry) {
        this.weightEntry = weightEntry;
    }

    public void setWeightKg(float weightKg) {
        if(weightEntry == null) {
            weightEntry = new WeightEntry();
        }
        weightEntry.setWeightKg(weightKg);
    }

    public float getWeightKg() {
        return (weightEntry != null) ? weightEntry.getWeightKg() : 0;
    }


    /* PARSER
     * ----------------------------------------------------------------------------------------------*/
    @Override
    public String idAlias() {
        return UserEntry.ALIAS_ID;
    }

    @Override
    public User fromCursor(Cursor cursor) {

        User user = null;

        try {
            int INDEX_ID = cursor.getColumnIndex(UserEntry.ALIAS_ID);
            int INDEX_NAME = cursor.getColumnIndex(UserEntry.COLUMN_NAME);
            int INDEX_EMAIL = cursor.getColumnIndex(UserEntry.COLUMN_EMAIL);
            int INDEX_BIRTHDAY = cursor.getColumnIndex(UserEntry.COLUMN_BIRTHDAY);
            int INDEX_AVATAR_PATH = cursor.getColumnIndex(UserEntry.COLUMN_AVATAR_PATH);
            int INDEX_GENDER = cursor.getColumnIndex(UserEntry.COLUMN_GENDER);
            int INDEX_BODY_STRUCTURE = cursor.getColumnIndex(UserEntry.COLUMN_BODY_STRUCTURE);
            int INDEX_LIFE_STYLE = cursor.getColumnIndex(UserEntry.COLUMN_LIFE_STYLE);
            int INDEX_WEIGHT_UNIT = cursor.getColumnIndex(UserEntry.COLUMN_WEIGHT_UNIT);
            int INDEX_HEIGHT_UNIT = cursor.getColumnIndex(UserEntry.COLUMN_HEIGHT_UNIT);
            int INDEX_HEIGHT_CM = cursor.getColumnIndex(UserEntry.COLUMN_HEIGHT_CM);
            int INDEX_WEIGHT_KG = cursor.getColumnIndex(UserEntry.JOINED_WEIGHT_KG);
            int INDEX_WEIGHT_USER_ID = cursor.getColumnIndex(UserEntry.JOINED_WEIGHT_USER_ID);
            int INDEX_WEIGHT_DATE = cursor.getColumnIndex(UserEntry.JOINED_WEIGHT_DATE);
            int INDEX_WEIGHT_BMI = cursor.getColumnIndex(UserEntry.JOINED_WEIGHT_BMI);
            int INDEX_WEIGHT_ID = cursor.getColumnIndex(WooserContract.WeightEntryEntry.ALIAS_ID);

            long id = cursor.getLong(INDEX_ID);
            String name = cursor.getString(INDEX_NAME);
            String email = cursor.getString(INDEX_EMAIL);
            long birthday = cursor.getLong(INDEX_BIRTHDAY);
            String avatarPath = cursor.getString(INDEX_AVATAR_PATH);
            @Gender.Def int gender = cursor.getInt(INDEX_GENDER);
            @BodyStructure.Def int bodyStructure = cursor.getInt(INDEX_BODY_STRUCTURE);
            @LifeStyle.Def int lifeStyle = cursor.getInt(INDEX_LIFE_STYLE);
            @WeightUnits.Def int weightUnit = cursor.getInt(INDEX_WEIGHT_UNIT);
            @HeightUnits.Def int heightUnit = cursor.getInt(INDEX_HEIGHT_UNIT);
            float heightCm = cursor.getFloat(INDEX_HEIGHT_CM);

            long weightId = cursor.getLong(INDEX_WEIGHT_ID);
            float weightKg = cursor.getFloat(INDEX_WEIGHT_KG);
            float weightBmi = cursor.getFloat(INDEX_WEIGHT_BMI);
            long weightUserId = cursor.getLong(INDEX_WEIGHT_USER_ID);
            long weightDate = cursor.getLong(INDEX_WEIGHT_DATE);

            WeightEntry weightEntry = new WeightEntry(weightId, weightUserId, weightDate, weightKg, weightBmi);

            user = new User();
            user.setId(id);
            user.setName(name);
            user.setBirthday(birthday);
            user.setEmail(email);
            user.setAvatarPath(avatarPath);
            user.setGender(gender);
            user.setBodyStructure(bodyStructure);
            user.setLifeStyle(lifeStyle);
            user.setWeightUnit(weightUnit);
            user.setHeightUnit(heightUnit);
            user.setHeightCentimeters(heightCm);
            user.setWeightEntry(weightEntry);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(UserEntry.COLUMN_NAME, this.getName());
        cv.put(UserEntry.COLUMN_BIRTHDAY, this.getBirthday());
        cv.put(UserEntry.COLUMN_EMAIL, this.getEmail());
        cv.put(UserEntry.COLUMN_AVATAR_PATH, this.getAvatarPath());
        cv.put(UserEntry.COLUMN_GENDER, this.getGender());
        cv.put(UserEntry.COLUMN_BODY_STRUCTURE, this.getBodyStructure());
        cv.put(UserEntry.COLUMN_LIFE_STYLE, this.getLifeStyle());
        cv.put(UserEntry.COLUMN_WEIGHT_UNIT, this.getWeightUnit());
        cv.put(UserEntry.COLUMN_HEIGHT_UNIT, this.getHeightUnit());
        cv.put(UserEntry.COLUMN_HEIGHT_CM, this.getHeightCentimeters());

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
        dest.writeInt(this.weightUnit);
        dest.writeInt(this.heightUnit);
        dest.writeInt(this.gender);
        dest.writeInt(this.bodyStructure);
        dest.writeInt(this.lifeStyle);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.avatarPath);
        dest.writeLong(this.birthday);
        dest.writeFloat(this.heightCentimeters);
        dest.writeParcelable(this.weightEntry, flags);
    }

    protected User(Parcel in) {
        setId(in.readLong());

        @WeightUnits.Def int weightUnit = in.readInt();
        this.weightUnit = weightUnit;

        @HeightUnits.Def int heightUnit = in.readInt();
        this.heightUnit = heightUnit;

        @Gender.Def int gender = in.readInt();
        this.gender = gender;

        @BodyStructure.Def int bodyStructure = in.readInt();
        this.bodyStructure = bodyStructure;

        @LifeStyle.Def int lifeStyle = in.readInt();
        this.lifeStyle = lifeStyle;

        this.name = in.readString();
        this.email = in.readString();
        this.avatarPath = in.readString();
        this.birthday = in.readLong();
        this.heightCentimeters = in.readFloat();
        this.weightEntry = in.readParcelable(WeightEntry.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
