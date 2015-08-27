package com.posture.tooloom.garysposture;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by fraw on 5/08/2015.
 */
public class FileHandler {
    FileOutputStream fos;
    private static final String FILENAME = "GarysPostureData";


    private static FileHandler singletonFileHandler;
    private FileHandler(Context mconext){
        Log.d("Gary:", "FileHandler object created");
        try {
            createFile(mconext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public static FileHandler getInstance(Context mcontext){
    if(singletonFileHandler == null){
        singletonFileHandler = new FileHandler(mcontext);
    }
    Log.d("Gary:", "FileHandler object returned");
    return singletonFileHandler;
}

    public void createFile(Context mcontext) throws IOException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fnametime = new SimpleDateFormat("yyyyMMdd-kkmm");
        String dateString = fnametime.format(calendar.getTime());

        File extDir;

        if (checkExternalStorage(mcontext)) {
            extDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS),"Gary");

            if(!extDir.exists()){
                extDir.mkdir();
            }

            Log.d("Gary:", "FileHandler " + extDir + "/" + FILENAME + dateString + ".csv");
            try {
                fos = new FileOutputStream(extDir + "/" + FILENAME + dateString + ".csv",true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else{
            extDir = new File(mcontext.getFilesDir(),FILENAME + dateString + ".csv");
            fos = new FileOutputStream(extDir);
        }


        // Write Header
        String csvText = "Timestamp,Z,AvgZ\n";
        fos.write(csvText.getBytes());
        Toast.makeText(mcontext, "File created : " + extDir, Toast.LENGTH_LONG).show();

    }

    public boolean checkExternalStorage(Context mcontext){
        String state = Environment.getExternalStorageState();
        switch (state) {
            case Environment.MEDIA_MOUNTED:
                return true;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                Toast.makeText(mcontext, "External Storage is read-only", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(mcontext, "External Storage is unavailable", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    public void closeFile(){
        Log.d("Gary:", "FileHandler closeFile");
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        singletonFileHandler = null;
    }

}
