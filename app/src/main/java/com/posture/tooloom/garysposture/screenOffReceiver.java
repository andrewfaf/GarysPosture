package com.posture.tooloom.garysposture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by fraw on 7/08/2015.
 */
public class screenOffReceiver extends BroadcastReceiver{
    AccelHandler accelHandler;
//    Runnable mRunnable;
    Context mContext;

        @Override
        public void onReceive(final Context context, Intent intent) {
            AccelHandler accelHandler = AccelHandler.getInstance(context,1);
            Log.d("Gary:", "screenOffReceiver onReceive");
            this.mContext = context;
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                accelHandler.pauseAccel();
//                accelHandler.restartAccel();

                Handler mHandler = new Handler();

                mHandler.postDelayed(mRunnable, 100l);
            }
        }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Gary:", "screenOffReceiver Runnable");
            AccelHandler accelHandler = AccelHandler.getInstance(mContext,1);
            accelHandler.restartAccel();
        }
    };
}
