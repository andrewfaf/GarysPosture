package com.posture.tooloom.garysposture;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by fraw on 5/08/2015.
 */
public class PrefsHandler {
    private static PrefsHandler singletonPrefsHandler;
    private static SharedPreferences sharedPrefs;
    private Context mcontext;
    private static boolean vibrateFwdOn = true;
    private static boolean vibrateBwdOn = true;
    private static boolean keepScreenOn = true;
    private static int fwdThreshold = 5;
    private static int bwdThreshold = 5;
    private static double calibratedZ = 0;
    private static double aws;
    private static double updatesInterval = 5000;



    public static PrefsHandler getInstance(Context mcontext){
        if (singletonPrefsHandler == null){
            singletonPrefsHandler = new PrefsHandler(mcontext);
        }
        return singletonPrefsHandler;
    }


    public PrefsHandler(Context mcontext) {
        this.mcontext = mcontext;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.mcontext);
        SharedPreferences.OnSharedPreferenceChangeListener preflistener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {

                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        aws = (sharedPrefs.getFloat("EditTextAvgWindowSize", 4.0f));
                        Log.d("Gary:", "EditTextAvgWindowSize = " +
                                (sharedPrefs.getString("EditTextAvgWindowSize", "999")));
                        if (aws < 2) {
                            aws = 2;
                        }
                        vibrateFwdOn = sharedPrefs.getBoolean("checkBoxFwd", true);
                        vibrateBwdOn = sharedPrefs.getBoolean("checkBoxBwd", true);

                        Log.d("Gary:", "EditTextFwdThresh = " +
                                sharedPrefs.getString("EditTextFwdThresh", "5"));

                        Log.d("Gary:", "EditTextBwdThresh = " +
                                sharedPrefs.getString("EditTextBwdThresh", "5"));

                        fwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextFwdThresh", "5"));
                        bwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextBwdThresh", "5"));

                        keepScreenOn = sharedPrefs.getBoolean("checkBoxScreen", true);
                        updatesInterval = sharedPrefs.getFloat("updates_interval", 5000.0f);

                    }
                };
        sharedPrefs.registerOnSharedPreferenceChangeListener(preflistener);

        calibratedZ = (double) sharedPrefs.getFloat("CalibratedZ", 0.0f);
        aws = (double) sharedPrefs.getFloat("EditTextAvgWindowSize", 4.0f);
        vibrateFwdOn = sharedPrefs.getBoolean("checkBoxFwd", true);
        vibrateBwdOn = sharedPrefs.getBoolean("checkBoxBwd", true);
        fwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextFwdThresh", "5"));
        bwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextBwdThresh", "5"));
        updatesInterval = sharedPrefs.getFloat("updates_interval", 5000.0f);
    }

    public double getCalibratedZ() {
        return calibratedZ;
    }

    public double getAws() {
        return aws;
    }

    public boolean getvibrateFwdOn() {
        return vibrateFwdOn;
    }

    public boolean getvibrateBwdOn() {
        return vibrateBwdOn;
    }

    public double getfwdThreshold() {
        return fwdThreshold;
    }

    public double getbwdThreshold() {
        return bwdThreshold;
    }

    public boolean getKeepScreenOn() {
        return keepScreenOn;
    }
    public double getUpdatesInterval(){
        return updatesInterval;
    }
}

