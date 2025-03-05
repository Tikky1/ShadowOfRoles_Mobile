package com.kankangames.shadowofroles;

import android.app.Application;
import android.content.Context;

import com.kankangames.shadowofroles.managers.LanguageManager;
import com.kankangames.shadowofroles.managers.TextManager;

public class GameApplication extends Application {

    private static GameApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LanguageManager languageManager = new LanguageManager(this);
        languageManager.loadLocale();
    }

    @Override
    protected void attachBaseContext(Context base) {

        LanguageManager languageManager = new LanguageManager(base);
        super.attachBaseContext(languageManager.updateBaseContext(base));
    }

    public static Context getAppContext() {
        if (instance == null) {
            throw new IllegalStateException("Application instance is null. Ensure GameApplication is declared in AndroidManifest.xml.");
        }
        return instance.getApplicationContext();
    }

}
