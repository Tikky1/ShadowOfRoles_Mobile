package com.kankangames.shadowofroles.managers;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.kankangames.shadowofroles.GameApplication;
import com.kankangames.shadowofroles.ui.activities.MainActivity;

import java.util.Locale;

public class LanguageManager {

    @SuppressLint("StaticFieldLeak")
    private static LanguageManager instance;
    private Context context;
    private final String PREFS_NAME = "language_prefs";
    private final String KEY_LANGUAGE = "selected_language";

    private LanguageManager(Context context) {
        this.context = context;
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public String enumToStringXmlSuffix(String enumName, String suffix){
        enumName = enumName.toLowerCase(Locale.ROOT);
        enumName = String.format("%s_%s", enumName, suffix);
        return enumName;
    }

    public void setLocale(String languageCode) {
        saveLanguagePreference(languageCode);

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = this.context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        context = context.createConfigurationContext(config);
    }

    public void loadLocale() {
        SharedPreferences prefs = context.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("language", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public String getSavedLanguage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();
    }

    public String enumToStringXmlPrefix(String enumName, String prefix){
        enumName = enumName.toLowerCase(Locale.ROOT);
        enumName =String.format("%s_%s", prefix, enumName);
        return enumName;
    }

    public String enumToStringXml(String enumName){
        return enumName.toLowerCase(Locale.ROOT);
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

    public String getTextPrefix(String enumName, String prefix){
        return getText(enumToStringXmlPrefix(enumName, prefix));
    }

    public String getTextSuffix(String enumName, String prefix){
        return getText(enumToStringXmlSuffix(enumName, prefix));
    }

    public String getTextEnum(String enumName){
        return getText(enumToStringXml(enumName));
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
