package com.rolegame.game.models.roles.corrupterroles.analyst;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.InvestigativeAbility;
import com.rolegame.game.models.roles.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

public final class Darkseer extends CorrupterRole implements InvestigativeAbility {
    public Darkseer() {
        super(RoleID.Darkseer, AbilityType.PASSIVE,
                RolePriority.NONE, RoleCategory.CORRUPTER_ANALYST, 0, 0, false);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return performAbilityForPassiveRoles(roleOwner, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return darkSeerAbility(roleOwner, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(10,10);
    }
}
