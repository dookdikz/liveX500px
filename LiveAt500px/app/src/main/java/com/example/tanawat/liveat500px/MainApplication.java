package com.example.tanawat.liveat500px;

import android.app.Application;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by Tanawat on 22/9/2559.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

