package com.rolegame.game.models.roles.templates.neutralroles;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.RoleTemplate;

public abstract class NeutralRole extends RoleTemplate {
    public NeutralRole(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                       WinningTeam winningTeam ,double attack, double defence, boolean isRoleBlockImmune, boolean hasNormalWinCondition) {
        super(id, abilityType, rolePriority, roleCategory, winningTeam, attack, defence, isRoleBlockImmune, hasNormalWinCondition);
    }

    @Override
    public String getGoal() {
        return languageManager.getText(languageManager.enumToStringXml(id.name())+"_goal");
    }

    public abstract boolean canWinWithOtherTeams();
}
