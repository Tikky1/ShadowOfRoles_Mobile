package com.kankangames.shadowofroles.game.models.player;

import androidx.annotation.NonNull;

import com.kankangames.shadowofroles.game.gamestate.Time;
import com.kankangames.shadowofroles.game.gamestate.WinStatus;
import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.properties.DeathProperties;
import com.kankangames.shadowofroles.game.models.roles.properties.Role;

import java.util.Locale;
import java.util.Objects;

public abstract class Player{
    protected int number;
    protected String name;
    protected DeathProperties deathProperties;
    protected Role role;
    protected WinStatus winStatus;
    protected boolean isAI;

    public Player(int number, String name, boolean isAI) {
        this.number = number;
        this.name = name;
        this.deathProperties = new DeathProperties();
        this.isAI = isAI;
        winStatus = WinStatus.LOST;
    }

    public static AIPlayer makeAI(Player player){
        if(player.isAI){
            return (AIPlayer) player;
        }
        AIPlayer aiPlayer = new AIPlayer(player.number, player.name);
        aiPlayer.deathProperties = player.deathProperties;
        aiPlayer.role = player.role;
        aiPlayer.winStatus = player.winStatus;
        return aiPlayer;
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
        return String.format(Locale.ROOT,"(%d) %s", number, name);
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
        return getNameAndNumber() + " (" + role.getTemplate().getName() + ")";
    }

    public WinStatus getWinStatus() {
        return winStatus;
    }

    public void setWinStatus(WinStatus winStatus) {
        this.winStatus = winStatus;
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

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", deathProperties=" + deathProperties +
                ", role=" + role +
                ", winStatus=" + winStatus +
                ", isAI=" + isAI +
                '}';
    }
}
