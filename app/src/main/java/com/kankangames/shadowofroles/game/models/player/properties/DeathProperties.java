package com.kankangames.shadowofroles.game.models.player.properties;

import com.kankangames.shadowofroles.game.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;

import java.util.LinkedList;
import java.util.List;
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
        TextManager textManager = TextManager.getInstance();
        String deathTimeStr = textManager.getTextEnum(deathTime.name());

        return String.format(deathTimeStr,dayCountOfDeath);
    }

    public final String getCausesOfDeathAsString(){
        TextManager textManager = TextManager.getInstance();
        return causesOfDeath.stream()
                .map(causeOfDeath-> textManager.getTextPrefix(causeOfDeath.name(),"cause_of_death"))
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
