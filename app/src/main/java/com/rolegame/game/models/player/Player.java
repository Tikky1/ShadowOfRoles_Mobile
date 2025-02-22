package com.rolegame.game.models.player;

import com.rolegame.game.gamestate.CauseOfDeath;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.Role;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Player {
    private final int number;
    private final String name;
    private Role role;
    private boolean isAlive;
    private double attack;
    private double defence;
    private boolean hasWon;
    private final LinkedList<CauseOfDeath> causesOfDeath;
    private boolean isImmune;
    private boolean isRevealed;

    public Player(int number, String name) {
        this.number = number;
        this.name = name;
        this.isAlive = true;
        this.isRevealed = false;
        hasWon = false;
        causesOfDeath = new LinkedList<>();
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

    public final String getCausesOfDeathAsString(){
        LanguageManager languageManager = LanguageManager.getInstance();
        return causesOfDeath.stream()
                .map( causeOfDeath->languageManager.getText("cause_of_death_"+languageManager.enumToStringXml(causeOfDeath.name())))
                .collect(Collectors.joining(", "));
    }

    @Override
    public final String toString(){
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

    public final boolean isAlive() {
        return isAlive;
    }

    public final void setAlive(boolean alive) {
        isAlive = alive;
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

    public final LinkedList<CauseOfDeath> getCausesOfDeath() {
        return causesOfDeath;
    }
    public final void setImmune(boolean isImmune){
        this.isImmune = isImmune;
    }

    public final boolean isImmune(){
        return isImmune;
    }

    public final void addCauseOfDeath(CauseOfDeath causeOfDeath) {
        causesOfDeath.add(causeOfDeath);
    }

    public final boolean isRevealed() {
        return isRevealed;
    }

    public final void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public abstract boolean isAIPlayer();
}
