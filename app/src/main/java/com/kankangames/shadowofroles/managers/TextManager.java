package com.kankangames.shadowofroles.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.kankangames.shadowofroles.GameApplication;

import java.util.Locale;

public class TextManager {

    @SuppressLint("StaticFieldLeak")
    private static TextManager instance;
    private final Context context;


    private TextManager(Context context) {
        this.context = context;
    }

    public static TextManager getInstance() {
        if (instance == null) {
            instance = new TextManager(GameApplication.getAppContext());
        }
        return instance;
    }

    public String enumToStringXmlSuffix(String enumName, String suffix){
        enumName = enumName.toLowerCase(Locale.ROOT);
        enumName = String.format("%s_%s", enumName, suffix);
        return enumName;
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

        Locale locale = new Locale(LanguageManager.getInstance().getSavedLanguage());
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Context localizedContext = context.createConfigurationContext(config);

        return localizedContext.getResources().getString(context.getResources().getIdentifier(key, "string", context.getPackageName()));
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


}
