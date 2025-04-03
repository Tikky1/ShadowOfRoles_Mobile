package com.kankangames.shadowofroles.utils.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;

import com.kankangames.shadowofroles.GameApplication;
import com.kankangames.shadowofroles.game.models.settings.Language;

import java.util.Locale;

public final class TextManager {

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
        enumName = enumName.toLowerCase(Locale.ENGLISH);
        enumName = String.format("%s_%s", enumName, suffix);
        return enumName;
    }



    public String enumToStringXmlPrefix(String enumName, String prefix){
        enumName = enumName.toLowerCase(Locale.ENGLISH);
        enumName = String.format("%s_%s", prefix, enumName);
        return enumName;
    }

    public String enumToStringXml(String enumName){
        return enumName.toLowerCase(Locale.ROOT);
    }

    public String getText(String key) {

        return getText(key, LanguageManager.getInstance().getSavedLanguage());
    }

    private String getText(String key, String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Context localizedContext = context.createConfigurationContext(config);

        return localizedContext.getResources().getString(
                context.getResources().getIdentifier(key, "string", context.getPackageName()));
    }

    public String getText(String key, Language language){
        return getText(key, language.code());
    }

    public String getText(int resId){
        return context.getString(resId);
    }


    public String getTextEnumPrefix(String enumName, String prefix){
        return getText(enumToStringXmlPrefix(enumName, prefix));
    }

    public String getTextEnumPrefix(String enumName, String prefix, Language language){
        return getText(enumToStringXmlPrefix(enumName, prefix), language);
    }

    public String getTextEnumSuffix(String enumName, String prefix){
        return getText(enumToStringXmlSuffix(enumName, prefix));
    }

    public String getTextEnumSuffix(String enumName, String prefix, Language language){
        return getText(enumToStringXmlSuffix(enumName, prefix), language);
    }

    public String getTextEnum(String enumName){
        return getText(enumToStringXml(enumName));
    }

    public String getTextEnum(String enumName, Language language){
        return getText(enumToStringXml(enumName), language);
    }


}
