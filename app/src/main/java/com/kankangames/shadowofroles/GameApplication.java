package com.kankangames.shadowofroles;

import android.app.Application;
import android.content.Context;

import com.kankangames.shadowofroles.managers.LanguageManager;

public class GameApplication extends Application {

    private static GameApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LanguageManager languageManager = LanguageManager.getInstance();
        languageManager.loadLocale();

    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
