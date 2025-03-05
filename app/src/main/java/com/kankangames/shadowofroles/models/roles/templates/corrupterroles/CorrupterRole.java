package com.kankangames.shadowofroles.models.roles.templates.corrupterroles;

import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public abstract class CorrupterRole extends RoleTemplate {
    public CorrupterRole(RoleID id, AbilityType abilityType,
                         RolePriority rolePriority, RoleCategory roleCategory, double attack , double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, WinningTeam.CORRUPTER, attack, defence, isRoleBlockImmune, true);
    }

    @Override
    public String getGoal() {
        return textManager.getText("goal_corrupter");
    }
}
