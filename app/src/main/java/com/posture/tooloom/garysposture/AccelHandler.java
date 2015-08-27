package com.posture.tooloom.garysposture;

import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;

import android.util.Log;
import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by fraw on 2/07/2015.
 * Implements accelerometer handler
 */
//public class AccelHandler implements SensorEventListener {
    public class AccelHandler implements org.openintents.sensorsimulator.hardware.SensorEventListener {

    private static AccelHandler singletonAccelHandler;
/*
    private SensorManager sensorManager;
    private Sensor accel;
*/

    private  SensorManagerSimulator sensorManager;
    private org.openintents.sensorsimulator.hardware.Sensor accel;

    private boolean started = false;
    private long lastSaved = System.currentTimeMillis();
    private double filteredZ = 0;
    private double totalZ = 0;
    private double z = 0;
    private double zcount = 0;
    private double sessionTimeTotal;
    private long sessionTimeStart;
    private long sessionTimeStop;
    private PrefsHandler prefsHandler;
    private double sampleTime;
    private double aws;
    private double fwdThreshold;
    private double bwdThreshold;
    private double calibratedZ;
    private double samplesfwd;
    private double samplesbwd;

    private AccelHandler (Context mContext,double sampleTime) {
//        sensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager = SensorManagerSimulator.getSystemService(mContext, SENSOR_SERVICE);
        sensorManager.connectSimulator();

        prefsHandler = PrefsHandler.getInstance(mContext);

        this.sampleTime = sampleTime;
        fwdThreshold = prefsHandler.getfwdThreshold()/2;
        bwdThreshold = -prefsHandler.getbwdThreshold()/2;

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d("Gary:", "AccelHandler create Object");

    }

    public void startAccel() {
        started = true;
        calibratedZ =prefsHandler.getCalibratedZ();
        aws =prefsHandler.getAws();
        totalZ = 0;
        z = 0;
        zcount = 0;
        samplesfwd = 0;
        samplesbwd =0;
        sessionTimeStart = System.currentTimeMillis();

//        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accel, SensorManagerSimulator.SENSOR_DELAY_NORMAL);
        Log.d("Gary:", "AccelHandler startAccel");

    }

    public static AccelHandler getInstance(Context mContext,double sampleTime){
            if(singletonAccelHandler == null){
                singletonAccelHandler = new AccelHandler(mContext,sampleTime);
            }
            Log.d("Gary:", "AccelHandler getInstance");
            return singletonAccelHandler;
    }

    public void stopAccel(){
        started = false;
        sensorManager.unregisterListener(this);
        sessionTimeStop = System.currentTimeMillis();
        sessionTimeTotal =  sessionTimeStop - sessionTimeStart;
//        singletonAccelHandler = null;
        Log.d("Gary:", "AccelHandler stopAccel");
    }

    public void pauseAccel(){
        if (started) {
            sensorManager.unregisterListener(this);
            Log.d("Gary:", "AccelHandler pauseAccel");
        }
    }

    public void restartAccel(){
        if (started) {
//            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, accel, SensorManagerSimulator.SENSOR_DELAY_NORMAL);
            Log.d("Gary:", "AccelHandler restartAccel");
        }
    }
    public boolean getStarted(){
        return started;
    }
    public double getFilteredZ(){
        return filteredZ;
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
    public double getSessionAverageZ(){
        Log.d("Gary:" , "Average SessionZ " + (getAverageZ() - calibratedZ) );
        Log.d("Gary:", "sessionTimeTotal " + getSessionTimeTotal());
        Log.d("Gary:", "Average Sample Time " + getSessionTimeTotal()/zcount);
        Log.d("Gary:", "Samples Above Forward Threshold " + samplesfwd);
        Log.d("Gary:", "Samples Below Backward Threshold " + samplesbwd);
        return (getAverageZ() - calibratedZ);
    }
    public double getSessionTimeTotal(){ return sessionTimeTotal; }
    public double getTotalSamples() { return zcount; }
    public double getSamplesFwd(){ return samplesfwd; }
    public double getSamplesBwd(){
        return samplesbwd;
    }
    public double getAvgSampleTime(){ return getSessionTimeTotal()/zcount; }
    public long getSessionTimeStart(){ return sessionTimeStart; }
    public long getSessionTimeStop(){ return sessionTimeStop; }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
//            Log.d("Gary:", "AccelHandler Triggered " + System.currentTimeMillis());
            if ((System.currentTimeMillis() - lastSaved) > sampleTime) {
                lastSaved = System.currentTimeMillis();
                z = event.values[2];
                totalZ += z;
                zcount += 1;
                z -= calibratedZ;
                if (z > fwdThreshold){
                    samplesfwd++;
                }else if (z < bwdThreshold){
                    samplesbwd++;
                }

                // Moving window average
                filteredZ -= filteredZ /(aws-1);
                filteredZ += z/aws;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
