package com.rolegame.game.models.roles.neutralroles.chaos;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.NoAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.neutralroles.NeutralRole;
import com.rolegame.game.services.GameService;


public final class ChillGuy extends NeutralRole implements NoAbility {
    public ChillGuy() {
        super(RoleID.ChillGuy, AbilityType.NO_ABILITY, RolePriority.NONE, RoleCategory.NEUTRAL_CHAOS, 0, 0, false);

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
        return new ChanceProperty(10,1);
    }


    @Override
    public boolean canWinWithOtherTeams() {
        return true;
    }
}
