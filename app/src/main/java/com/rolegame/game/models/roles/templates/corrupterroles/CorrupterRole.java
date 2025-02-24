package com.rolegame.game.models.roles.templates.corrupterroles;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public abstract class CorrupterRole extends RoleTemplate {
    public CorrupterRole(RoleID id, AbilityType abilityType,
                         RolePriority rolePriority, RoleCategory roleCategory, double attack , double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, WinningTeam.CORRUPTER, attack, defence, isRoleBlockImmune, true);
    }

    @Override
    public String getGoal() {
        return languageManager.getText("goal_corrupter");
    }
}
