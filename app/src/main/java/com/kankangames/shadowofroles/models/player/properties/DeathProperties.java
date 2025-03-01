package com.kankangames.shadowofroles.models.player.properties;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.LanguageManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DeathProperties {
    private List<CauseOfDeath> causesOfDeath;
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

        String deathTimeStr = deathTime.name().substring(0, 1).toUpperCase(Locale.ROOT) +
                deathTime.name().substring(1).toLowerCase(Locale.ROOT);

        return deathTimeStr + ": " + dayCountOfDeath;
    }

    public final String getCausesOfDeathAsString(){
        LanguageManager languageManager = LanguageManager.getInstance();
        return causesOfDeath.stream()
                .map( causeOfDeath->languageManager.getText("cause_of_death_"+languageManager.enumToStringXml(causeOfDeath.name())))
                .collect(Collectors.joining(", "));
    }

    // Getters and Setters
    public List<CauseOfDeath> getCausesOfDeath() {
        return causesOfDeath;
    }

    public void setCausesOfDeath(List<CauseOfDeath> causesOfDeath) {
        this.causesOfDeath = causesOfDeath;
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
