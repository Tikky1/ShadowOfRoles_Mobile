package com.rolegame.game.models.roles.templates.folkroles;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public abstract class FolkRole extends RoleTemplate {
    public FolkRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                    double attack , double defence, boolean isRoleBlockImmune) {
        super(id, abilityType, rolePriority, roleCategory, Team.FOLK, attack, defence, isRoleBlockImmune, true);
    }

    @Override
    public String getGoal() {
        return LanguageManager.getInstance().getText("goal_folk_role");
    }
}
