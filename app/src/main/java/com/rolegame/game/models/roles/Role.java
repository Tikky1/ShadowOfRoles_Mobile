package com.rolegame.game.models.roles;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public class Role {

    protected RoleTemplate template;
    protected Player roleOwner;
    protected Player choosenPlayer;
    protected boolean canPerform;
    protected AbilityResult abilityResult;

    public Role(RoleTemplate template) {

        this.template = template;
        this.canPerform = true;
    }

    public Role(Role role) {

        this.canPerform = true;
        this.roleOwner = role.roleOwner;
        this.choosenPlayer = roleOwner.getRole().getChoosenPlayer();
        this.abilityResult = role.abilityResult;
        this.template = role.template;
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

    public final AbilityResult getAbilityResult() {
        return abilityResult;
    }

    public final void setAbilityResult(AbilityResult abilityResult) {
        this.abilityResult = abilityResult;
    }
}
