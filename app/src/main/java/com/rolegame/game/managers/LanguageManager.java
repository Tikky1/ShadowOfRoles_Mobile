package com.rolegame.game.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.rolegame.game.GameApplication;

import java.util.Locale;

public class LanguageManager {

    // Is not a memory leak because the application object is always in the memory already
    @SuppressLint("StaticFieldLeak")
    private static LanguageManager instance;
    private final Context context;

    private LanguageManager(Context context) {
        this.context = context;
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public String enumToStringXml(String enumName){
        enumName = enumName.toLowerCase(Locale.ROOT);
        return enumName;
    }

    public String getText(String key) {
        if (key == null || key.isEmpty()) {
            Log.w("LanguageManager", "Invalid key: null or empty");
            return "Undefined key";
        }

        int resourceId = context.getResources().getIdentifier(key, "string", context.getPackageName());

        if (resourceId > 0) {
            return context.getString(resourceId);
        } else {
            Log.w("LanguageManager", "String resource not found for key: " + key);
            return "Undefined: " + key;
        }
    }



}
