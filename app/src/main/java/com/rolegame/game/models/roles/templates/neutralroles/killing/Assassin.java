package com.rolegame.game.models.roles.templates.neutralroles.killing;

import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.AttackAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.services.GameService;

public final class Assassin extends NeutralRole implements AttackAbility {
    public Assassin() {
        super(RoleID.Assassin, AbilityType.ACTIVE_OTHERS, RolePriority.NONE,
                RoleCategory.NEUTRAL_KILLING, WinningTeam.ASSASSIN, 1, 1, true,true);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {

       return performAbilityForBlockImmuneRoles(roleOwner, choosenPlayer, gameService);

    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return attack(roleOwner,choosenPlayer, gameService, CauseOfDeath.ASSASSIN);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(40, 1);
    }

    @Override
    public boolean canWinWithOtherTeams() {
        return false;
    }
}

