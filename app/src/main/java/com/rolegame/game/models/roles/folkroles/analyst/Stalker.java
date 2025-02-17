package com.rolegame.game.models.roles.folkroles.analyst;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.InvestigativeAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.folkroles.FolkRole;
import com.rolegame.game.services.GameService;

public final class Stalker extends FolkRole implements InvestigativeAbility {

    public Stalker() {
        super(RoleID.Stalker, AbilityType.ACTIVE_OTHERS,
                RolePriority.NONE, RoleCategory.FOLK_ANALYST, 0, 0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return stalkerAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25,10);
    }
}
