package com.kankangames.shadowofroles.game.models.roles.properties;

public class RoleProperties {

    // Base stats
    protected double attack = 0;
    protected double defence = 0;
    protected int voteCount = 1;
    protected int money = -1;

    // General
    protected boolean isRoleBlockImmune = false;
    protected boolean canRoleBlock = false;
    protected boolean knowsTeamMembers = false;
    protected boolean hasPostDeathEffect = false;
    protected boolean revivesAfterDeath = false;


    // Abilities
    protected boolean hasHealingAbility = false;
    protected boolean hasAttackAbility = false;
    protected boolean canKill1v1 = false;
    protected boolean hasBlindAbility = false;
    protected boolean hasDisguiseAbility = false;
    protected boolean hasImmuneAbility = false;
    protected int abilityUsesLeft = -1;
    protected int cooldown = 0;


    // Game end
    protected boolean winsAlone = false;
    protected boolean mustBeLastStanding = false;
    protected boolean canWinWithAnyTeam = false;
    protected boolean mustSurviveUntilEnd = false;
    protected boolean mustDieToWin = false;
    protected boolean hasNormalWinCondition = true;


    public RoleProperties() {}


    public void incrementMoney(int money){
        this.money += money;
    }

    public void decrementMoney(int money){
        this.money -= money;
    }

    public void decrementAbilityUsesLeft(){
        abilityUsesLeft--;
    }

    public RoleProperties setWinsAlone(boolean winsAlone) {
        this.winsAlone = winsAlone;
        return this;
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

    public RoleProperties setRoleBlockImmune(boolean roleBlockImmune) {
        isRoleBlockImmune = roleBlockImmune;
        return this;
    }

    public RoleProperties setHasNormalWinCondition(boolean hasNormalWinCondition) {
        this.hasNormalWinCondition = hasNormalWinCondition;
        return this;
    }

    public RoleProperties setCanRoleBlock(boolean canRoleBlock) {
        this.canRoleBlock = canRoleBlock;
        return this;
    }

    public RoleProperties setHasHealingAbility(boolean hasHealingAbility) {
        this.hasHealingAbility = hasHealingAbility;
        return this;
    }

    public RoleProperties setHasAttackAbility(boolean hasAttackAbility) {
        this.hasAttackAbility = hasAttackAbility;
        return this;
    }

    public RoleProperties setHasBlindAbility(boolean hasBlindAbility) {
        this.hasBlindAbility = hasBlindAbility;
        return this;
    }

    public RoleProperties setHasDisguiseAbility(boolean hasDisguiseAbility) {
        this.hasDisguiseAbility = hasDisguiseAbility;
        return this;
    }

    public RoleProperties setHasImmuneAbility(boolean hasImmuneAbility) {
        this.hasImmuneAbility = hasImmuneAbility;
        return this;
    }

    public RoleProperties setAbilityUsesLeft(int abilityUsesLeft) {
        this.abilityUsesLeft = abilityUsesLeft;
        return this;
    }



    public RoleProperties setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public RoleProperties setKnowsTeamMembers(boolean knowsTeamMembers) {
        this.knowsTeamMembers = knowsTeamMembers;
        return this;
    }

    public RoleProperties setMustBeLastStanding(boolean mustBeLastStanding) {
        this.mustBeLastStanding = mustBeLastStanding;
        return this;
    }

    public RoleProperties setCanWinWithAnyTeam(boolean canWinWithAnyTeam) {
        this.canWinWithAnyTeam = canWinWithAnyTeam;
        return this;
    }

    public RoleProperties setMustSurviveUntilEnd(boolean mustSurviveUntilEnd) {
        this.mustSurviveUntilEnd = mustSurviveUntilEnd;
        return this;
    }

    public RoleProperties setHasPostDeathEffect(boolean hasPostDeathEffect) {
        this.hasPostDeathEffect = hasPostDeathEffect;
        return this;
    }

    public RoleProperties setMustDieToWin(boolean mustDieToWin) {
        this.mustDieToWin = mustDieToWin;
        return this;
    }

    public RoleProperties setRevivesAfterDeath(boolean revivesAfterDeath) {
        this.revivesAfterDeath = revivesAfterDeath;
        return this;
    }

    public RoleProperties setMoney(int money) {
        this.money = money;
        return this;
    }


    public RoleProperties setCanKill1v1(boolean canKill1v1) {
        this.canKill1v1 = canKill1v1;
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

    public boolean isRoleBlockImmune() {
        return isRoleBlockImmune;
    }

    public boolean hasNormalWinCondition() {
        return hasNormalWinCondition;
    }

    public boolean canRoleBlock() {
        return canRoleBlock;
    }

    public boolean winsAlone() {
        return winsAlone;
    }

    public boolean hasHealingAbility() {
        return hasHealingAbility;
    }

    public boolean hasAttackAbility() {
        return hasAttackAbility;
    }

    public boolean hasBlindAbility() {
        return hasBlindAbility;
    }

    public boolean hasDisguiseAbility() {
        return hasDisguiseAbility;
    }

    public boolean hasImmuneAbility() {
        return hasImmuneAbility;
    }

    public int abilityUsesLeft() {
        return abilityUsesLeft;
    }

    public int cooldown() {
        return cooldown;
    }

    public boolean knowsTeamMembers() {
        return knowsTeamMembers;
    }

    public boolean mustBeLastStanding() {
        return mustBeLastStanding;
    }

    public boolean canWinWithAnyTeam() {
        return canWinWithAnyTeam;
    }

    public boolean mustSurviveUntilEnd() {
        return mustSurviveUntilEnd;
    }

    public boolean hasPostDeathEffect() {
        return hasPostDeathEffect;
    }

    public boolean mustDieToWin() {
        return mustDieToWin;
    }

    public boolean revivesAfterDeath() {
        return revivesAfterDeath;
    }

    public int money() {
        return money;
    }

    public boolean canWin1v1() {
        return canKill1v1;
    }
}
