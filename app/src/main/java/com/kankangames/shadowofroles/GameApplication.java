package com.kankangames.shadowofroles;

import android.app.Application;
import android.content.Context;

public class GameApplication extends Application {

    private static GameApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
