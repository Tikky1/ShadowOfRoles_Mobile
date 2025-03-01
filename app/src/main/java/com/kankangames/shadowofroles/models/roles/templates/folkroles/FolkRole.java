package com.kankangames.shadowofroles.models.roles.templates.folkroles;

import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public abstract class FolkRole extends RoleTemplate {
    public FolkRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                    double attack , double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, WinningTeam.FOLK, attack, defence, isRoleBlockImmune, true);
    }

    @Override
    public String getGoal() {
        return languageManager.getText("goal_folk");
    }
}
