package com.example.ac.attestation.myapplication;

import android.app.Application;
import android.content.Context;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class MyApplication extends Application {
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        autosizeconfig();
    }

    private void autosizeconfig() {
        AutoSizeConfig.getInstance().setCustomFragment(true);
        AutoSize.initCompatMultiProcess(context);
    }

    private Context myapplication() {
        return context;
    }
}
