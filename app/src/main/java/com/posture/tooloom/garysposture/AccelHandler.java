package com.posture.tooloom.garysposture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by fraw on 2/07/2015.
 * Implements accelerometer handler
 */
public class AccelHandler implements SensorEventListener{
    private static AccelHandler singletonAccelHandler;
    private SensorManager sensorManager;
    private boolean started = false;
    private long lastSaved = System.currentTimeMillis();
    private ArrayList<AccelData> sensorData;
    private double LongTermAverage = 0;
    private Sensor accel;
    private double totalZ = 0;
    private double z = 0;
    private double zcount = 0;
    private PrefsHandler prefsHandler;
    private double sampleTime;
    private double aws;
    private double calibratedZ;

    private AccelHandler (Context mContext,double sampleTime) {
        sensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        prefsHandler = PrefsHandler.getInstance(mContext);

        this.sampleTime = sampleTime;
        aws =prefsHandler.getAws();
        calibratedZ =prefsHandler.getCalibratedZ();

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startAccel();

    }

    public void startAccel() {
        started = true;
        totalZ = 0;
        z = 0;
        zcount = 0;

        sensorManager.registerListener(this, accel,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static AccelHandler getInstance(Context mContext,double sampleTime){
            if(singletonAccelHandler == null){
                singletonAccelHandler = new AccelHandler(mContext,sampleTime);
            }
            return singletonAccelHandler;
    }

    public void stopAccel(){
        started = false;
        sensorManager.unregisterListener(this);

    }

    public void pauseAccel(){
        if (started) {
            sensorManager.unregisterListener(this);
        }
    }

    public void restartAccel(){
        if (started) {
            sensorManager.registerListener(this, accel,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    public double getLongTermAverage(){
        return LongTermAverage;
    }
    public double getZ(){
        return z;
    }

    public double getAverageZ (){
        Log.d("Gary:" , "Number of samples " + zcount);
        double avgZ;
        avgZ = totalZ;
        avgZ /= zcount;
        Log.d("Gary:" , "totalZ " + totalZ);
        Log.d("Gary:", "avgZ " + avgZ);
        return avgZ;
        }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
            if ((System.currentTimeMillis() - lastSaved) > sampleTime) {
                lastSaved = System.currentTimeMillis();
                z = event.values[2];
                totalZ += z;
                zcount += 1;
                z -= calibratedZ;

                // Moving window average
                LongTermAverage -= LongTermAverage/(aws-1);
                LongTermAverage += z/aws;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
