package com.kankangames.shadowofroles.models.roles.templates.folkroles.protector;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.ProtectiveAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class Soulbinder extends FolkRole implements ProtectiveAbility {
    public Soulbinder() {
        super(RoleID.Soulbinder, AbilityType.ACTIVE_OTHERS, RolePriority.HEAL,
                RoleCategory.FOLK_PROTECTOR, 0,0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return heal(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(20, 10);
    }
}
