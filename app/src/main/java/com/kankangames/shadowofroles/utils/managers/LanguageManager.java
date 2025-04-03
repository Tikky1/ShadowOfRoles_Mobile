package com.kankangames.shadowofroles.utils.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.kankangames.shadowofroles.GameApplication;
import com.kankangames.shadowofroles.game.models.settings.Language;
import com.kankangames.shadowofroles.game.models.settings.UserSettings;

import java.util.Locale;

public final class LanguageManager {

    @SuppressLint("StaticFieldLeak")
    private static LanguageManager instance;
    private Context context;

    public LanguageManager(Context context) {
        this.context = context;
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public void setLocale(Activity activity, Language language) {
        saveLanguagePreference(language);

        Locale locale = new Locale(language.code());
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context = context.createConfigurationContext(config);

        restartActivity(activity);
    }

    public void loadLocale() {
        UserSettings userSettings =  SettingsManager.getSettings(context);

        Locale locale = new Locale(userSettings.language().code());
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        context.createConfigurationContext(config);
    }

    public Language getSavedLanguage() {
        return SettingsManager.getSettings(context).language();
    }

    private void saveLanguagePreference(Language language) {
        UserSettings settings = SettingsManager.getSettings(context);
        settings.setLanguage(language);
        SettingsManager.saveSettings(context, settings);
    }

    public Context updateBaseContext(Context baseContext) {
        String language = getSavedLanguage().code();
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
