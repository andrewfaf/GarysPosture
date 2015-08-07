package com.posture.tooloom.garysposture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Handler;

/**
 * Created by fraw on 7/08/2015.
 */
public class screenOffReceiver extends BroadcastReceiver{
//    AccelHandler accelHandler;

        @Override

        public void onReceive(Context context, Intent intent) {
            AccelHandler accelHandler = AccelHandler.getInstance(context,1);
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                accelHandler.pauseAccel();
                accelHandler.restartAccel();
/*
                Handler mHandler = null;

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        accelHandler.restartAccel();}}, 100L);
*/


            }
        }
}
