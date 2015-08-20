package com.posture.tooloom.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by fraw on 18/08/2015.
 */
public class ProgressTable {
    // Table
    public static final String TABLE_PROGRESS = "progress";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SAMPLES = "samples";
    public static final String COLUMN_START_TIME = "starttime";
    public static final String COLUMN_STOP_TIME = "stoptime";
    public static final String COLUMN_SESSION_TIME = "sessiontime";
    public static final String COLUMN_TIME_FORWARD = "timeforward";
    public static final String COLUMN_TIME_BACKWARD = "timebackward";
    public static final String COLUMN_SAMPLES_FORWARD = "samplesforward";
    public static final String COLUMN_SAMPLES_BACKWARD = "samplesbackward";

    // Create SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PROGRESS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SAMPLES + " numeric not null, "
            + COLUMN_START_TIME + " timestamp not null, "
            + COLUMN_STOP_TIME + " timestamp not null, "
            + COLUMN_SESSION_TIME + " integer not null, "
            + COLUMN_TIME_FORWARD + " integer not null, "
            + COLUMN_SAMPLES_FORWARD + " integer not null, "
            + COLUMN_TIME_BACKWARD + " integer not null, "
            + COLUMN_SAMPLES_BACKWARD + " integer not null "
            + ");";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(DatabaseHandler.class.getName(), "Upgrading database from version "
                + i + " to " + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL(("DROP TABLE IF EXISTS ") + TABLE_PROGRESS);
        onCreate(sqLiteDatabase);
    }

}
