package com.skipservices.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


public class Propellez implements ProActivityLifecycleHelper.ActivityCallbacks{
    private final Context context;

    public Propellez(Context context) {
        this.context = ProActivityLifecycleHelper.wrap(context).with(this);
    }

    @Override
    public void OnSaved(Bundle savedInstance) {
        Log.d("helloss","");
        // doingit
//        takeScreenshot(a);
    }
}
