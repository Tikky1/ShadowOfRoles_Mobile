package com.rolegame.game.models.roles.templates.corrupterroles;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public abstract class CorrupterRole extends RoleTemplate {
    public CorrupterRole(RoleID id, AbilityType abilityType,
                         RolePriority rolePriority, RoleCategory roleCategory, double attack , double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, Team.CORRUPTER, attack, defence, isRoleBlockImmune);
    }

    @Override
    public String getGoal() {
        return LanguageManager.getInstance().getText("goal_corrupter");
    }
}
