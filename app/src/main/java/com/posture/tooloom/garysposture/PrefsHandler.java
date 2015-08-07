package com.posture.tooloom.garysposture;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Encapsulates the Persistent Configuration Data for the Application.
 * It is implemented as a Singelton Class so that all other Classes and Activities
 * access the same information and so that multiple unnecessary objects don't get created
 * When the Preferences are updated the local variables are updated also.
 * Getter methods allow the Applications other Activities and Classes to access these values
 */
public class PrefsHandler {
    private static PrefsHandler singletonPrefsHandler;
    private static SharedPreferences sharedPrefs;
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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mcontext);

        calibratedZ = (double) sharedPrefs.getFloat("CalibratedZ", 0.0f);
        aws = (double) Integer.parseInt(sharedPrefs.getString("EditTextAvgWindowSize", "4"));
        vibrateFwdOn = sharedPrefs.getBoolean("checkBoxFwd", true);
        vibrateBwdOn = sharedPrefs.getBoolean("checkBoxBwd", true);
        fwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextFwdThresh", "5"));
        bwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextBwdThresh", "5"));
        updatesInterval = Integer.parseInt(sharedPrefs.getString("updates_interval", "5000"));
        keepScreenOn = sharedPrefs.getBoolean("checkBoxScreen", true);
        Log.d("Gary:", "CalibratedZ = " + calibratedZ);
        Log.d("Gary:", "Average Window Size = " + aws);
        Log.d("Gary:", "Vibrate Forward On = " + vibrateFwdOn);
        Log.d("Gary:", "Vibrate Backward On = " + vibrateBwdOn);
        Log.d("Gary:", "EditTextFwdThresh = " + fwdThreshold);
        Log.d("Gary:", "EditTextBwdThresh = " + bwdThreshold);
        Log.d("Gary:", "Keep Screen On = " + keepScreenOn);
        Log.d("Gary:", "Updates Interval = " + updatesInterval);

        SharedPreferences.OnSharedPreferenceChangeListener preflistener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {

                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {

                        calibratedZ = (double) sharedPrefs.getFloat("CalibratedZ", 0.0f);
                        aws = Integer.parseInt(sharedPrefs.getString("EditTextAvgWindowSize", "4"));
                        Log.d("Gary:", "EditTextAvgWindowSize = " +
                                (sharedPrefs.getString("EditTextAvgWindowSize", "4")));
                        if (aws < 2) {
                            aws = 2;
                        }
                        vibrateFwdOn = sharedPrefs.getBoolean("checkBoxFwd", true);
                        vibrateBwdOn = sharedPrefs.getBoolean("checkBoxBwd", true);
                        fwdThreshold = Integer.parseInt(sharedPrefs.getString(
                                "EditTextFwdThresh", "5"));
                        bwdThreshold = Integer.parseInt(sharedPrefs.getString(
                                "EditTextBwdThresh", "5"));

                        keepScreenOn = sharedPrefs.getBoolean("checkBoxScreen", true);
                        updatesInterval = Integer.parseInt(sharedPrefs.getString(
                                "updates_interval", "5000"));

                        Log.d("Gary:", "Changed CalibratedZ = " + calibratedZ);
                        Log.d("Gary:", "Changed Average Window Size = " + aws);
                        Log.d("Gary:", "Changed Vibrate Forward On = " + vibrateFwdOn);
                        Log.d("Gary:", "Changed Vibrate Backward On = " + vibrateBwdOn);
                        Log.d("Gary:", "Changed EditTextFwdThresh = " + fwdThreshold);
                        Log.d("Gary:", "Changed EditTextBwdThresh = " + bwdThreshold);
                        Log.d("Gary:", "Changed Keep Screen On = " + keepScreenOn);
                        Log.d("Gary:", "Changed Updates Interval = " + updatesInterval);

                    }
                };
        sharedPrefs.registerOnSharedPreferenceChangeListener(preflistener);
    }

    public double getCalibratedZ() {
        calibratedZ = (double) sharedPrefs.getFloat("CalibratedZ", 0.0f);
        return calibratedZ;
    }

    public double getAws() {
        aws = (double) Integer.parseInt(sharedPrefs.getString("EditTextAvgWindowSize", "4"));
        return aws;
    }

    public boolean getvibrateFwdOn() {
        vibrateFwdOn = sharedPrefs.getBoolean("checkBoxFwd", true);
        return vibrateFwdOn;
    }

    public boolean getvibrateBwdOn() {
        vibrateBwdOn = sharedPrefs.getBoolean("checkBoxBwd", true);
        return vibrateBwdOn;
    }

    public double getfwdThreshold() {
        fwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextFwdThresh", "5"));
        return fwdThreshold;
    }

    public double getbwdThreshold() {
        bwdThreshold = Integer.parseInt(sharedPrefs.getString("EditTextBwdThresh", "5"));
        return bwdThreshold;
    }

    public boolean getKeepScreenOn() {
        keepScreenOn = sharedPrefs.getBoolean("checkBoxScreen", true);
        return keepScreenOn;
    }
    public double getUpdatesInterval(){
        updatesInterval = Integer.parseInt(sharedPrefs.getString("updates_interval", "5000"));
        return updatesInterval;
    }
}

