package com.skipservices.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.multidex.MultiDex;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;

public class MyPersonalApp extends Application {
    SharedPreferences Complete,token_id,TaskFinished,ImageBitmap;
    private static MyPersonalApp mInstance;
    public static final String TAG = MyPersonalApp.class
            .getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;
    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     */
    public static MyPersonalApp get() {
        return mInstance;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }

    public void onCreate() {
        super.onCreate();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Propellez usage=new Propellez(getApplicationContext());
        if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/SkipLogcatFolder" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }

            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }

            // clear the previous logcat and then write the new one to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else if ( isExternalStorageReadable() ) {
            // only readable
        } else {
            // not accessible
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }
    public String GetToken() {
        token_id = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        String token = token_id.getString("Guid", "");
        return token;
    }

    public void SetToken(String Token) {
        token_id = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        SharedPreferences.Editor editor = token_id.edit();
        //---save the values in the EditText view to preferences---
        editor.putString("Guid", Token);
        //---saves the values---
        editor.commit();
    }

    public String GetTaskFinished() {
        TaskFinished = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        String token = TaskFinished.getString("TaskFinished", "");
        return token;
    }

    public void SetTaskFinished(String Token) {
        TaskFinished = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        SharedPreferences.Editor editor = TaskFinished.edit();
        //---save the values in the EditText view to preferences---
        editor.putString("TaskFinished", Token);
        //---saves the values---
        editor.commit();
    }

    public String GetImageBitmap() {
        ImageBitmap = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        String token = ImageBitmap.getString("ImageBitmap", "");
        return token;
    }

    public void SetImageBitmap(String Token) {
        ImageBitmap = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        SharedPreferences.Editor editor = ImageBitmap.edit();
        //---save the values in the EditText view to preferences---
        editor.putString("ImageBitmap", Token);
        //---saves the values---
        editor.commit();
    }

    public String GetComplete() {
        Complete = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        String token = Complete.getString("Complete", "");
        return token;
    }

    public void SetComplete(String Token) {
        Complete = getSharedPreferences("RainCreatives", MODE_PRIVATE);
        SharedPreferences.Editor editor = Complete.edit();
        //---save the values in the EditText view to preferences---
        editor.putString("Complete", Token);
        //---saves the values---
        editor.commit();
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }
}
