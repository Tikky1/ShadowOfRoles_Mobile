package com.rolegame.game.managers;

import java.util.Locale;

public class LanguageManager {

    public static String getText(String string, String string2){

        return string+"."+string2;
    }

    public static String getRoleText(String string, String string2){

        return string+"."+string2;
    }

    public static String enumToJsonKey(String enumName) {
        StringBuilder jsonKey = new StringBuilder();


        String[] parts = enumName.split("_");
        jsonKey.append(parts[0].toLowerCase(Locale.ROOT));
        for (int i = 1; i < parts.length; i++) {
            jsonKey.append(parts[i].substring(0, 1).toUpperCase(Locale.ROOT))
                    .append(parts[i].substring(1).toLowerCase(Locale.ROOT));
        }
        return jsonKey.toString();
    }
}
