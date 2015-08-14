package com.posture.tooloom.garysposture;

import android.content.Context;
import android.util.Log;

/**
 * Created by fraw on 14/08/2015.
 */
public class StatisticsHandler {
    private static StatisticsHandler singletonStatisticsHandler;
    double daySampleTime;
    double dayAboveFwd;
    double dayAboveBwd;
    Context mcontext;

    private StatisticsHandler(Context mcontext) {
        daySampleTime = 0;
        dayAboveFwd = 0;
        dayAboveBwd = 0;

    }
    public static StatisticsHandler getInstance(Context mcontext){
        if (singletonStatisticsHandler == null){
            singletonStatisticsHandler = new StatisticsHandler(mcontext);
        }
        Log.d("Gary:", "Statistics Handler getInstance");
        return singletonStatisticsHandler;
    }
    public void updateStatistics(){
        AccelHandler accelHandler = AccelHandler.getInstance(mcontext,100);
        daySampleTime += accelHandler.getSessionTimeTotal();
        dayAboveFwd += accelHandler.getSamplesFwd();
        dayAboveBwd += accelHandler.getSamplesBwd();
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