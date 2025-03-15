package com.kankangames.shadowofroles.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.kankangames.shadowofroles.GameApplication;

import java.util.Locale;

public final class LanguageManager {

    @SuppressLint("StaticFieldLeak")
    private static LanguageManager instance;
    private Context context;

    private static final String PREFS_NAME = "language_prefs";
    private static final String KEY_LANGUAGE = "selected_language";

    public LanguageManager(Context context) {
        this.context = context;
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public void setLocale(Activity activity, String languageCode) {
        saveLanguagePreference(languageCode);

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context = context.createConfigurationContext(config);

        restartActivity(activity);
    }

    public void loadLocale() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lang = prefs.getString(KEY_LANGUAGE, "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context.createConfigurationContext(config);
    }

    public String getSavedLanguage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "en");
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();
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

    private void restartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

}
