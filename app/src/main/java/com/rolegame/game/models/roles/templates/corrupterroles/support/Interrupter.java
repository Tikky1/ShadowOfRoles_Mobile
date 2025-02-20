package com.rolegame.game.models.roles.templates.corrupterroles.support;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.RoleBlockAbility;
import com.rolegame.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

public final class Interrupter extends CorrupterRole implements RoleBlockAbility {
    public Interrupter() {
        super(RoleID.Interrupter, AbilityType.ACTIVE_OTHERS,
                RolePriority.ROLE_BLOCK, RoleCategory.CORRUPTER_SUPPORT, 0, 0, true);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {

        return performAbilityForBlockImmuneRoles(roleOwner, choosenPlayer, gameService);

    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return roleBlock(choosenPlayer);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 10);
    }
}
