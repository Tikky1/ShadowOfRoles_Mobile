package com.rolegame.game.models.roles.neutralroles;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public abstract class NeutralRole extends RoleTemplate {
    public NeutralRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                       double attack, double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, Team.NEUTRAL, attack, defence, isRoleBlockImmune);
    }

    @Override
    public String getGoal() {
        return LanguageManager.getText(id.toString() ,"goal");
    }

    public abstract boolean canWinWithOtherTeams();
}
