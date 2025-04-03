package com.kankangames.shadowofroles.game.models.roles.properties;

import java.util.EnumSet;

public class RoleProperties {

    // Base stats
    protected double attack = 0;
    protected double defence = 0;
    protected int voteCount = 1;
    protected int money = -1;
    protected int abilityUsesLeft = -1;
    protected int cooldown = 0;

    protected EnumSet<RoleAttribute> roleAttributes = EnumSet.noneOf(RoleAttribute.class);


    public RoleProperties() {}

    public RoleProperties addAttribute(RoleAttribute attribute){
        roleAttributes.add(attribute);
        return this;
    }

    public RoleProperties removeAttribute(RoleAttribute attribute){
        roleAttributes.remove(attribute);
        return this;
    }

    public boolean hasAttribute(RoleAttribute attribute){
        return roleAttributes.contains(attribute);
    }


    public void incrementMoney(int money){
        this.money += money;
    }

    public void decrementMoney(int money){
        this.money -= money;
    }

    public void decrementAbilityUsesLeft(){
        abilityUsesLeft--;
    }


    public RoleProperties setAttack(double attack) {
        this.attack = attack;
        return this;
    }

    public RoleProperties setDefence(double defence) {
        this.defence = defence;
        return this;
    }

    public RoleProperties setVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public RoleProperties setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public RoleProperties setMoney(int money) {
        this.money = money;
        return this;
    }

    public RoleProperties setAbilityUsesLeft(int abilityUsesLeft) {
        this.abilityUsesLeft = abilityUsesLeft;
        return this;
    }

    public double attack() {
        return attack;
    }

    public double defence() {
        return defence;
    }

    public int voteCount() {
        return voteCount;
    }


    public int abilityUsesLeft() {
        return abilityUsesLeft;
    }

    public int cooldown() {
        return cooldown;
    }

    public int money() {
        return money;
    }

}
