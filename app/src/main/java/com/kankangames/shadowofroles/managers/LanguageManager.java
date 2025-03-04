package com.kankangames.shadowofroles.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.kankangames.shadowofroles.GameApplication;

import java.util.Locale;

public class LanguageManager {

    @SuppressLint("StaticFieldLeak")
    private static LanguageManager instance;
    private Context context;
    private static final String PREFS_NAME = "language_prefs";
    private static final String KEY_LANGUAGE = "selected_language";

    private LanguageManager(Context context) {
        this.context = context;
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public String enumToStringXml(String enumName) {
        return enumName.toLowerCase(Locale.ROOT);
    }

    public void setLocale(String languageCode) {
        saveLanguagePreference(languageCode); // Seçilen dili kaydet

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = this.context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = context.createConfigurationContext(config);
        } else {
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }

    public String getSavedLanguage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "en"); // Varsayılan olarak İngilizce
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();
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

    public Context updateBaseContext(Context baseContext) {
        String language = getSavedLanguage();
        Locale newLocale = new Locale(language);
        Locale.setDefault(newLocale);

        Resources res = baseContext.getResources();
        Configuration config = res.getConfiguration();
        config.setLocale(newLocale);

        return baseContext.createConfigurationContext(config);
    }
}
