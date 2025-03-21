package com.kankangames.shadowofroles.models.roles.templates.folkroles;

import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public abstract class FolkRole extends RoleTemplate {
    public FolkRole(RoleID id, AbilityType abilityType,
                    RolePriority rolePriority, RoleCategory roleCategory) {
        super(id, abilityType, rolePriority, roleCategory, WinningTeam.FOLK);

    }

    @Override
    public String getGoal() {
        return textManager.getText("goal_folk");
    }
}
