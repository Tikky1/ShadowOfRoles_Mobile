package com.kankangames.shadowofroles.models.roles;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public class Role {

    protected RoleTemplate template;
    protected Player roleOwner;
    protected Player choosenPlayer;
    protected boolean canPerform;
    protected AbilityResult abilityResult;

    protected double attack;
    protected double defence;
    protected boolean isImmune;
    protected boolean isRevealed;

    public Role(RoleTemplate template) {

        this.template = template;
        isRevealed = false;
        resetStates();
    }

    public final void resetStates(){
        choosenPlayer = null;
        defence = template.getDefence();
        attack = template.getAttack();
        canPerform = true;
        isImmune = false;
    }

    public final Player getChoosenPlayer() {
        return choosenPlayer;
    }

    public final void setChoosenPlayer(final Player choosenPlayer) {
        this.choosenPlayer = choosenPlayer;
    }

    public final Player getRoleOwner() {
        return roleOwner;
    }

    public final void setRoleOwner(final Player roleOwner) {
        this.roleOwner = roleOwner;
    }

    public final boolean isCanPerform() {
        return canPerform;
    }

    public final void setCanPerform(final boolean canPerform) {
        this.canPerform = canPerform;
    }

    public final RoleTemplate getTemplate() {
        return template;
    }

    public final void setTemplate(RoleTemplate template) {
        this.template = template;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getDefence() {
        return defence;
    }

    public void setDefence(double defence) {
        this.defence = defence;
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public final AbilityResult getAbilityResult() {
        return abilityResult;
    }

    public final void setAbilityResult(AbilityResult abilityResult) {
        this.abilityResult = abilityResult;
    }
}
