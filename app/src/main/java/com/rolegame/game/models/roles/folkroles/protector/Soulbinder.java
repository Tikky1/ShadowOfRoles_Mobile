package com.rolegame.game.models.roles.folkroles.protector;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.ProtectiveAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.folkroles.FolkRole;
import com.rolegame.game.services.GameService;

public final class Soulbinder extends FolkRole implements ProtectiveAbility {
    public Soulbinder() {
        super(RoleID.Soulbinder, AbilityType.ACTIVE_OTHERS, RolePriority.HEAL,
                RoleCategory.FOLK_PROTECTOR, 0,0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return heal(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(20,10);
    }
}
