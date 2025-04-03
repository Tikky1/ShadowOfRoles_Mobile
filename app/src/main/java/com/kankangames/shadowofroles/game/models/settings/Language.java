package com.kankangames.shadowofroles.game.models.settings;

public enum Language {
    ENGLISH("en", "English"),
    TURKISH("tr", "Türkçe");

    final String code;
    final String text;

    Language(String code, String text){
        this.code = code;
        this.text = text;
    }

    public String code() {
        return code;
    }

    public String text() {
        return text;
    }
}
