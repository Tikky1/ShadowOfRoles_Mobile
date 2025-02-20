package com.rolegame.game.models.roles.templates.corrupterroles.analyst;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.InvestigativeAbility;
import com.rolegame.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

public final class DarkRevealer extends CorrupterRole implements InvestigativeAbility {
    public DarkRevealer() {
        super(RoleID.DarkRevealer,  AbilityType.OTHER_THAN_CORRUPTER
                ,RolePriority.NONE, RoleCategory.CORRUPTER_ANALYST, 0, 0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return darkRevealerAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 10);
    }
}
