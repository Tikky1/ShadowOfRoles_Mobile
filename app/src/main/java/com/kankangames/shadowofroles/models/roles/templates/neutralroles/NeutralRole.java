package com.kankangames.shadowofroles.models.roles.templates.neutralroles;

import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public abstract class NeutralRole extends RoleTemplate {
    public NeutralRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                       WinningTeam winningTeam ,double attack, double defence, boolean isRoleBlockImmune, boolean hasNormalWinCondition) {
        super(id, abilityType, rolePriority, roleCategory, winningTeam, attack, defence, isRoleBlockImmune, hasNormalWinCondition);
    }

    @Override
    public String getGoal() {
        return languageManager.getTextSuffix(id.name(),"goal");
    }

    public abstract boolean canWinWithOtherTeams();
}
