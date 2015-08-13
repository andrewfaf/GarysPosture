package com.posture.tooloom.garysposture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by fraw on 7/08/2015.
 */
public class screenOffReceiver extends BroadcastReceiver {
    AccelHandler accelHandler;
    Context mContext;
    private boolean screen_off  = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        AccelHandler accelHandler = AccelHandler.getInstance(context, 1);
        Log.d("Gary:", "screenOffReceiver onReceive");
        this.mContext = context;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screen_off = true;
            if (accelHandler.getStarted()) {
                accelHandler.pauseAccel();
                Handler mHandler = new Handler();
                mHandler.postDelayed(mRunnable, 100l);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screen_off = false;
        }
    }
public boolean getScreenState(){
    return screen_off;
}
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Gary:", "screenOffReceiver Runnable");
            AccelHandler accelHandler = AccelHandler.getInstance(mContext, 1);
            accelHandler.restartAccel();
        }


    };
}
