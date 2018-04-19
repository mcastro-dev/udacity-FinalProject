package com.mistdev.google_drive.listeners;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mcastro on 11/04/17.
 */

public class ErrorCode {
    public static final String UNKNOWN = "Unknown error";
    public static final String NOT_CONNECTED = "GoogleDrive is not connected";
    public static final String RETRIEVING_FILE = "Error retrieving file";
    public static final String CREATING_FILE = "Error creating file";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ UNKNOWN, NOT_CONNECTED, RETRIEVING_FILE, CREATING_FILE })
    public @interface Def { }
}