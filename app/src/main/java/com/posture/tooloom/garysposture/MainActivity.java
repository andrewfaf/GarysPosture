package com.posture.tooloom.garysposture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;



/* Done
    Save Data - Done for Saving to SDCard
    Save Data in csv format - Done
    Change Calibrate to a button in the settings/Action Bar? - Done
    Check Box to keep screen on for Accelerometer to work on some Phones - Done
    Calibrated value is not saved to sharedPrefs. - Done
    Labels in graph seem to be swapped. - Done
    Check Boxes showing correct state in Preferences Setting Menu - Done
    Set a reliable location to save the files - Done
    Change so that file is created when Start pressed and data is appended,
    reduces chance of lost data when crash occurs and reduces memory use. - Done
    Make sure file gets closed on exit. - Done
    Set timestamp to be actual time instead of system tic count - Done
    Graph shows time on X axis - Done
*/

/* To Do
todo    Reset accel values when startAccel() is called so that the instantaneous and filtered
        values are not influenced by the previous session in the case
        where you stop and then start a new session.
todo    Change to a service for phones that don't need the screen on.
todo    Need to be able to select a saved data set and graph it
todo    Need to be able to select a group of data sets (or one) and email csv formatted versions
todo    Drop Box or Google Drive the files
*/

public class MainActivity extends Activity implements OnClickListener {
    private Button btnStart, btnStop, btnGraph;
    private TextView txtAvg;

    public static boolean vibrateFwdOn = true;
    public static boolean vibrateBwdOn = true;
    public static int fwdThreshold = 5;
    public static int bwdThreshold = 5;
    public static double aws;

    private static float brightness = 0.1f;
    private boolean started = false;

    AlertMonitor alertMonitor;
    PrefsHandler prefsHandler;
    FileHandler fileHandler;
    AccelHandler laccelHandler;

    private static Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnGraph = (Button) findViewById(R.id.btnGraph);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnGraph.setOnClickListener(this);
        btnGraph.setEnabled(false);

        txtAvg = (TextView) findViewById(R.id.textView);

        alertMonitor = new AlertMonitor(this);
        prefsHandler = PrefsHandler.getInstance(this);

        mHandler = new Handler();
        aws = prefsHandler.getAws();

//        Toast.makeText(this, "Calibrated Value is " + calibratedZ, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, 0, 0, "Change settings");
        menu.add(Menu.NONE, 1, 0, "Calibrate");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(this, PrefsActivity.class);
                startActivity(intent);
                return true;
            case 1:
                startActivity(new Intent(this, CalibrateActivity.class));
                return true;
        }
        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private Runnable mrunnable = new Runnable() {
        @Override
        public void run() {

//            txtAvg.setText(String.format("%.2f",laccelHandler.getLongTermAverage()));
            mHandler.postDelayed(this,500);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Gary:", "MainActivity onPause");
        if (started) {
                fileHandler.closeFile();
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alertMonitor.killHandlers();
        if (started) {
            fileHandler.closeFile();
        }
   }


    @Override
    public void onClick(View v) {
        Window w = getWindow();
        WindowManager.LayoutParams lp;
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                btnGraph.setEnabled(false);
                fileHandler = FileHandler.getInstance(this);
                started = true;
                // Wait 5 seconds before starting.
                alertMonitor.startHandlers();
                laccelHandler = AccelHandler.getInstance(this,prefsHandler.getUpdatesInterval());
                mHandler.post(mrunnable);
                if (prefsHandler.getKeepScreenOn()){
                    w.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    lp = w.getAttributes();
                    brightness = lp.screenBrightness;
                    lp.screenBrightness = 0.1f;
// 1 Seems to be full brightness, 0 is off which seems to be the same as turning off the screen
// and you can't easily turn it back on
                    w.setAttributes(lp);
                }

                break;
            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnGraph.setEnabled(true);
                started = false;
                alertMonitor.killHandlers();
                mHandler.removeCallbacks(mrunnable);
                if (prefsHandler.getKeepScreenOn()) {
                    w.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    w = getWindow();
                    lp = w.getAttributes();
                lp.screenBrightness = brightness;
//                    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
                    w.setAttributes(lp);
                }

                    fileHandler.closeFile();
                break;
            case R.id.btnGraph:
                Intent i = new Intent(this, GraphActivity.class);
                i.putExtra("data", alertMonitor.sensorData);
                startActivity(i);
                break;
            default:
                break;
        }

    }

}
