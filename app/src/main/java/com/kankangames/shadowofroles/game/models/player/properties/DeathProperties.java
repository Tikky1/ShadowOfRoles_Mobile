package com.kankangames.shadowofroles.game.models.player.properties;

import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.utils.managers.TextManager;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DeathProperties {
    private final List<CauseOfDeath> causesOfDeath;
    private final TimePeriod deathTimePeriod;
    private boolean isAlive;

    public DeathProperties() {
        causesOfDeath = new LinkedList<>();
        deathTimePeriod = TimePeriod.of(Time.DAY, -1);
        isAlive = true;
    }

    public void addCauseOfDeath(CauseOfDeath causeOfDeath){
        causesOfDeath.add(causeOfDeath);
    }

    public String getDeathTimeAndDayCount(){
        TextManager textManager = TextManager.getInstance();
        String deathTimeStr = textManager.getTextEnum(deathTimePeriod.time().name());

        return String.format(deathTimeStr, deathTimePeriod.dayCount());
    }

    public final String getCausesOfDeathAsString(){
        TextManager textManager = TextManager.getInstance();
        return causesOfDeath.stream()
                .map(causeOfDeath-> textManager.getTextEnumPrefix(causeOfDeath.name(),"cause_of_death"))
                .collect(Collectors.joining(", "));
    }

    // Getters and Setters
    public List<CauseOfDeath> getCausesOfDeath() {
        return causesOfDeath;
    }

    public int getDayCountOfDeath() {
        return deathTimePeriod.dayCount();
    }

    public void setDayCountOfDeathDay(int dayCountOfDeath) {
        deathTimePeriod.setDayCount(dayCountOfDeath);
    }

    public void setDayCountOfDeathNight(int dayCountOfDeath) {
        deathTimePeriod.setDayCount(dayCountOfDeath-1);
    }

    public Time getDeathTime() {
        return deathTimePeriod.time();
    }

    public void setDeathTime(Time deathTimePeriod) {
        this.deathTimePeriod.setTime(deathTimePeriod);
    }

    public TimePeriod getDeathTimePeriod() {
        return deathTimePeriod;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }



}
