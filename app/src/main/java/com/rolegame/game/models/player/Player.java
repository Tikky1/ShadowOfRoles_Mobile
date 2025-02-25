package com.rolegame.game.models.player;

import com.rolegame.game.gamestate.Time;
import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.models.player.properties.DeathProperties;
import com.rolegame.game.models.roles.Role;

import java.util.Objects;

public abstract class Player {
    private final int number;
    private final String name;
    private Role role;
    private double attack;
    private double defence;
    private boolean hasWon;
    private final DeathProperties deathProperties;
    private boolean isImmune;
    private boolean isRevealed;

    public Player(int number, String name) {
        this.number = number;
        this.name = name;
        this.deathProperties = new DeathProperties();
        this.isRevealed = false;
        hasWon = false;
    }

    public void killPlayer(Time deathTime, int deathDayCount, CauseOfDeath causeOfDeath){
        deathProperties.setAlive(false);
        deathProperties.setDeathTime(deathTime);
        deathProperties.addCauseOfDeath(causeOfDeath);
        deathProperties.setDayCountOfDeath(deathDayCount);
    }

    public final void setRole(Role role) {
        this.role = role;
        this.role.setRoleOwner(this);
        resetStates();
    }
    public final void resetStates(){
        this.getRole().setChoosenPlayer(null);
        this.setDefence(this.getRole().getTemplate().getDefence());
        this.setAttack(this.getRole().getTemplate().getAttack());
        this.getRole().setCanPerform(true);
        this.setImmune(false);
    }

    public final String getNameAndNumber(){
        return number +". " +name;
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
        return number + ". " + name + " (" + role.getTemplate().getName()+ ")";
    }


    public final double getAttack() {
        return attack;
    }

    public final void setAttack(double attack) {
        this.attack = attack;
    }

    public final double getDefence() {
        return defence;
    }

    public final void setDefence(double defence) {
        this.defence = defence;
    }

    public final boolean isHasWon() {
        return hasWon;
    }

    public final void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public final void setImmune(boolean isImmune){
        this.isImmune = isImmune;
    }

    public final boolean isImmune(){
        return isImmune;
    }


    public final boolean isRevealed() {
        return isRevealed;
    }

    public final void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public DeathProperties getDeathProperties() {
        return deathProperties;
    }

    public abstract boolean isAIPlayer();
}
