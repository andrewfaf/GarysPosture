package com.posture.tooloom.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.posture.tooloom.garysposture.AccelHandler;
import com.posture.tooloom.garysposture.R;

import static java.lang.StrictMath.abs;


public class CalibrateActivity extends Activity {

    private AccelHandler cAccelHandler;
    private Handler cHandler;
    private boolean delayFlag = false;
    private Button btnCalibrate;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        btnCalibrate = (Button) findViewById(R.id.calibrateButton);
        cHandler = new Handler();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    }


    public void doCalibrate(View v) {
        Log.d("Gary:", "Calibrate Activity doCalibrate");
        btnCalibrate.setEnabled(false);
        cAccelHandler = AccelHandler.getInstance(this, 100);
        cAccelHandler.startAccel();
        delayFlag = false;
        cHandler.postDelayed(crunnable, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Gary:", "Calibrate Activity onResume");
//        cAccelHandler.restartAccel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Gary:", "Calibrate Activity onPause");
//		cAccelHandler.pauseAccel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Gary:", "Calibrate Activity onDestroy");
//        cAccelHandler.stopAccel();
    }


    private Runnable crunnable = new Runnable() {
        @Override
        public void run() {
            long[] vpattern = {0, 200, 100, 400, 100, 200, 0};
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            Log.d("Gary:", "delayFlag is " + delayFlag);
            if(!delayFlag){
                delayFlag = true;
                v.vibrate(vpattern, -1);
                cHandler.postDelayed(crunnable, 5000);
                return;
            }
            delayFlag = false;
            cAccelHandler.stopAccel();
            cHandler.removeCallbacks(crunnable);
            v.vibrate(vpattern, -1);
//            MainActivity.calibratedZ = cAccelHandler.getAverageZ();
            Log.d("Gary:", "CalibratedZ " + cAccelHandler.getAverageZ());

            sharedPrefs.edit().putFloat("CalibratedZ",(float)cAccelHandler.getAverageZ()).apply();

            finish();
        }
    };

}
