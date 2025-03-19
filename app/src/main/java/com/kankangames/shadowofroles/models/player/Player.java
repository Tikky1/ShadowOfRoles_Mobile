package com.kankangames.shadowofroles.models.player;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.player.properties.DeathProperties;
import com.kankangames.shadowofroles.models.roles.Role;

import java.util.Locale;
import java.util.Objects;

public abstract class Player{
    protected final int number;
    protected final String name;
    protected final DeathProperties deathProperties;
    protected Role role;
    protected boolean hasWon;
    protected boolean isAI;

    public Player(int number, String name, boolean isAI) {
        this.number = number;
        this.name = name;
        this.deathProperties = new DeathProperties();
        this.isAI = isAI;
        hasWon = false;
    }

    public void killPlayer(Time deathTime, int deathDayCount, CauseOfDeath causeOfDeath, boolean isDay){
        deathProperties.setAlive(false);
        deathProperties.setDeathTime(deathTime);
        deathProperties.addCauseOfDeath(causeOfDeath);

        if(isDay) deathProperties.setDayCountOfDeathDay(deathDayCount);
        else deathProperties.setDayCountOfDeathNight(deathDayCount);
    }

    public final void setRole(Role role) {
        this.role = role;
    }


    public final String getNameAndNumber(){
        return String.format(Locale.ROOT,"%d.%s", number, name);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return number == player.number;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(number);
    }

    public final int getNumber() {
        return number;
    }

    public final String getName() {
        return name;
    }

    public final Role getRole() {
        return role;
    }

    public final String getNameAndRole(){
        return String.format(Locale.ROOT,"%d.%s (%s)", number, name ,role.getTemplate().getName());
    }

    public final boolean isHasWon() {
        return hasWon;
    }

    public final void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public DeathProperties getDeathProperties() {
        return deathProperties;
    }

    public boolean isAIPlayer(){
        return isAI;
    }

    public boolean isSamePlayer(Player player){
        return player.getNumber() == number;
    }

    public boolean isSamePlayer(int number){
        return this.number == number;
    }

    @Override
    public String toString() {
        return "Player{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", deathProperties=" + deathProperties +
                ", role=" + role +
                ", hasWon=" + hasWon +
                ", isAI=" + isAI +
                '}';
    }
}
