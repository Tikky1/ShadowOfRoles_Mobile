package com.rolegame.game.models.roles.templates.neutralroles.chaos;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.NoAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.services.GameService;

public final class Clown extends NeutralRole implements NoAbility {
    public Clown() {
        super(RoleID.Clown, AbilityType.NO_ABILITY, RolePriority.NONE, RoleCategory.NEUTRAL_CHAOS,
               WinningTeam.CLOWN ,0, 0,  false, false);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return performAbilityForNoAbilityRoles();
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return doNothing();
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 1);
    }

    @Override
    public boolean canWinWithOtherTeams() {
        return true;
    }
}
