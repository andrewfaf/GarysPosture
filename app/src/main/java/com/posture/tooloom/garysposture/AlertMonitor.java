package com.posture.tooloom.garysposture;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fraw on 4/08/2015.
 */
public class AlertMonitor {
    private static Handler vibHandler;
    AccelHandler lAccelHandler;
    public ArrayList<AccelData> sensorData;
    private Context mContext;
    private PrefsHandler prefsHandler;
    private FileHandler fileHandler;


    public AlertMonitor(Context mcontext){
        this.mContext = mcontext;
        vibHandler = new Handler();
        sensorData = new ArrayList<AccelData>();
        prefsHandler = PrefsHandler.getInstance(this.mContext);
        fileHandler =FileHandler.getInstance(mcontext);
    }

    private Runnable vibrunnable = new Runnable() {
        @Override

        public void run() {
            long[] vpatternf = {0, 200, 200, 200, 200, 200, 0};
            long[] vpatternb = {0, 400, 200, 400, 0};

            long currentTime = System.currentTimeMillis();
            double upper = prefsHandler.getfwdThreshold()/2;
            double lower = -prefsHandler.getbwdThreshold()/2;
            double vbn = prefsHandler.getUpdatesInterval();
            Date resultDate = new Date(currentTime);

            Log.d("Gary:", "AlertMonitor Triggered");
            AccelData data = new AccelData(resultDate, lAccelHandler.getZ(),
                    lAccelHandler.getFilteredZ(), upper,lower, currentTime);

            sensorData.add(data);

            String text = data.toCsv();

            try {
                fileHandler.fos.write(text.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            if ((lAccelHandler.getFilteredZ() > upper) && prefsHandler.getvibrateFwdOn()) {
                v.vibrate(vpatternf, -1);
            } else if ((lAccelHandler.getFilteredZ() < lower) &&
                    prefsHandler.getvibrateBwdOn()) {
                v.vibrate(vpatternb, -1);
            }
            vibHandler.postDelayed(this, (long) vbn);
        }
    };

    public void startHandlers(){
        vibHandler.postDelayed(vibrunnable, 5000);
        lAccelHandler = AccelHandler.getInstance(mContext,500);

    }

    public void killHandlers(){
        lAccelHandler.stopAccel();
        vibHandler.removeCallbacks(vibrunnable);
    }

}
