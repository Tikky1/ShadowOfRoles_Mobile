package com.kankangames.shadowofroles.game.models.settings;

public class UserSettings {

    private Language language;
    private String username;

    public UserSettings(Language language, String username) {
        this.language = language;
        this.username = username;
    }

    public Language language() {
        return language;
    }

    public UserSettings setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public String username() {
        return username;
    }

    public UserSettings setUsername(String username) {
        this.username = username;
        return this;
    }
}
