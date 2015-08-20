package com.posture.tooloom.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.posture.tooloom.garysposture.AccelData;
import com.posture.tooloom.garysposture.AccelHandler;
import com.posture.tooloom.garysposture.R;
import com.posture.tooloom.garysposture.StatisticsHandler;

import java.util.ArrayList;


public class StatisticsActivity extends ActionBarActivity {
    ArrayList<AccelData> sensorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        sensorData = (ArrayList<AccelData>)getIntent().getSerializableExtra("data");
        AccelHandler accelHandler = AccelHandler.getInstance(this, 100);
        TextView sessionTime = (TextView) findViewById(R.id.totalSessionTimetextView);
        sessionTime.setText(String.format("Session Time %.0f sec",
                accelHandler.getSessionTimeTotal() / 1000));

        TextView avgSampleTime = (TextView) findViewById(R.id.avgSampleTimetextView);
        avgSampleTime.setText(String.format("Avg Sample %.1f ms",
                accelHandler.getAvgSampleTime()));

        TextView aboveFWDThreshold = (TextView) findViewById(R.id.timeOverFWDThresholdtextView);
        aboveFWDThreshold.setText(String.format("Forward  %.0f sec",
                accelHandler.getSamplesFwd()*accelHandler.getAvgSampleTime()/1000));

        TextView aboveBWDThreshold = (TextView) findViewById(R.id.timeOverBWDThresholdtextView);
        aboveBWDThreshold.setText(String.format("Backward %.0f sec",
                accelHandler.getSamplesBwd() * accelHandler.getAvgSampleTime() / 1000));

        StatisticsHandler statisticsHandler = StatisticsHandler.getInstance(this, sensorData);
        TextView daySessionTime = (TextView) findViewById(R.id.daySessionTimetextView);
        daySessionTime.setText(String.format("Session Time %.0f sec",
                statisticsHandler.getDaySampleTime()/1000));

        TextView dayAboveFWDThreshold = (TextView) findViewById(R.id.dayOverFwdThresholdtextView);
        dayAboveFWDThreshold.setText(String.format("Forward  %.0f sec",
                statisticsHandler.getDayAboveFwd()*accelHandler.getAvgSampleTime()/1000));

        TextView dayAboveBWDThreshold = (TextView) findViewById(R.id.dayOverBwdThresholdtextView);
        dayAboveBWDThreshold.setText(String.format("Backward %.0f sec",
                statisticsHandler.getDayAboveBwd()*accelHandler.getAvgSampleTime()/1000));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
