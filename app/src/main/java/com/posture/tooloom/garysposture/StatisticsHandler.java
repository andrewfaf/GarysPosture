package com.posture.tooloom.garysposture;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.posture.tooloom.db.DatabaseHandler;
import com.posture.tooloom.db.ProgressTable;

import java.util.ArrayList;

/**
 * Created by fraw on 14/08/2015.
 */
public class StatisticsHandler {
    private static StatisticsHandler singletonStatisticsHandler;
    double daySampleTime;
    double dayAboveFwd;
    double dayAboveBwd;
    Context mcontext;
    DatabaseHandler progressDbHelper;
    ArrayList<AccelData> sensorData;


    private StatisticsHandler(Context mcontext, ArrayList<AccelData> sensorData) {
        this.mcontext = mcontext;
        daySampleTime = 0;
        dayAboveFwd = 0;
        dayAboveBwd = 0;
        this.sensorData = sensorData;

    }
    public static StatisticsHandler getInstance(Context mcontext, ArrayList<AccelData> sensorData){
        if (singletonStatisticsHandler == null){
            singletonStatisticsHandler = new StatisticsHandler(mcontext, sensorData);
        }
        Log.d("Gary:", "Statistics Handler getInstance");
        return singletonStatisticsHandler;
    }
    public void updateStatistics(){
        progressDbHelper = new DatabaseHandler(mcontext);
        SQLiteDatabase progressDb = progressDbHelper.getWritableDatabase();

        AccelHandler accelHandler = AccelHandler.getInstance(mcontext,100);
        daySampleTime += accelHandler.getSessionTimeTotal();
        dayAboveFwd += accelHandler.getSamplesFwd();
        dayAboveBwd += accelHandler.getSamplesBwd();

        ContentValues values = new ContentValues();
        values.put(ProgressTable.COLUMN_SAMPLES, 100);
        values.put(ProgressTable.COLUMN_START_TIME,
                String.valueOf(sensorData.get(0).getTimestamp()));
        values.put(ProgressTable.COLUMN_STOP_TIME,
                String.valueOf(sensorData.get(0).getTimestamp()));
        values.put(ProgressTable.COLUMN_SESSION_TIME,100);
        values.put(ProgressTable.COLUMN_TIME_FORWARD,100);
        values.put(ProgressTable.COLUMN_SAMPLES_FORWARD, (int)(accelHandler.getSamplesFwd()));
        values.put(ProgressTable.COLUMN_TIME_BACKWARD,100);
        values.put(ProgressTable.COLUMN_SAMPLES_BACKWARD, (int)(accelHandler.getSamplesBwd()));

        long newRowId = progressDb.insert(
                ProgressTable.TABLE_PROGRESS,
                null,
                values);
    }
    public double getDaySampleTime(){
        return daySampleTime;
    }
    public double getDayAboveFwd(){
        return dayAboveFwd;
    }
    public double getDayAboveBwd(){
        return dayAboveBwd;
    }
}