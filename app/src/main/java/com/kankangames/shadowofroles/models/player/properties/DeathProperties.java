package com.kankangames.shadowofroles.models.player.properties;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.LanguageManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DeathProperties {
    private final List<CauseOfDeath> causesOfDeath;
    private int dayCountOfDeath;
    private Time deathTime;
    private boolean isAlive;

    public DeathProperties() {
        causesOfDeath = new LinkedList<>();
        dayCountOfDeath = -1;
        deathTime = null;
        isAlive = true;
    }

    public void addCauseOfDeath(CauseOfDeath causeOfDeath){
        causesOfDeath.add(causeOfDeath);
    }

    public String getDeathTimeAndDayCount(){
        LanguageManager languageManager = LanguageManager.getInstance();
        String deathTimeStr = languageManager.getText(languageManager.enumToStringXml(deathTime.name()));

        return String.format(deathTimeStr,dayCountOfDeath);
    }

    public final String getCausesOfDeathAsString(){
        LanguageManager languageManager = LanguageManager.getInstance();
        return causesOfDeath.stream()
                .map(causeOfDeath->languageManager.getText("cause_of_death_"+languageManager.enumToStringXml(causeOfDeath.name())))
                .collect(Collectors.joining(", "));
    }

    // Getters and Setters
    public List<CauseOfDeath> getCausesOfDeath() {
        return causesOfDeath;
    }

    public int getDayCountOfDeath() {
        return dayCountOfDeath;
    }

    public void setDayCountOfDeathDay(int dayCountOfDeath) {
        this.dayCountOfDeath = dayCountOfDeath;
    }

    public void setDayCountOfDeathNight(int dayCountOfDeath) {
        this.dayCountOfDeath = dayCountOfDeath-1;
    }

    public Time getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(Time deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }



}
