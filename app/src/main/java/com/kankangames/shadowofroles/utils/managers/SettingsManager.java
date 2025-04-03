package com.kankangames.shadowofroles.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.settings.Language;
import com.kankangames.shadowofroles.game.models.settings.UserSettings;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;

import java.util.Locale;
import java.util.UUID;

public class SettingsManager {

    private static final String PREFS_NAME = "user_settings";

    public static void saveSettings(Context context, UserSettings userSettings){
        Gson gson = GsonProvider.getGson();
        String json = gson.toJson(userSettings);

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_settings_key", json);
        editor.apply();
    }

    public static UserSettings getSettings(Context context){
        Gson gson = GsonProvider.getGson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("user_settings_key", null);

        if(json == null){
            UserSettings userSettings = new UserSettings(Language.ENGLISH,
                    String.format(Locale.ROOT, context.getString(R.string.random_player_name),
                            UUID.randomUUID().toString().substring(0, 8)));
            saveSettings(context, userSettings);
            return userSettings;
        }
        return gson.fromJson(json, UserSettings.class);
    }
}
