package com.rolegame.game.models.roles.folkroles.support;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.RoleBlockAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.folkroles.FolkRole;
import com.rolegame.game.services.GameService;

public final class SealMaster extends FolkRole implements RoleBlockAbility {
    public SealMaster() {
        super(RoleID.SealMaster, AbilityType.ACTIVE_OTHERS, RolePriority.ROLE_BLOCK,
                RoleCategory.FOLK_SUPPORT, 0,0, true);
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
        return new ChanceProperty(25,10);
    }
}
