package com.kankangames.shadowofroles.models.roles.templates.neutralroles;

import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public abstract class NeutralRole extends RoleTemplate {
    public NeutralRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                       WinningTeam winningTeam) {
        super(id, abilityType, rolePriority, roleCategory, winningTeam);
    }

    @Override
    public String getGoal() {
        return textManager.getTextSuffix(id.name(),"goal");
    }

}
