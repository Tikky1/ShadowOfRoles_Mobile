package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.RoleBlockAbility;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class Interrupter extends CorrupterRole implements RoleBlockAbility {
    public Interrupter() {
        super(RoleID.Interrupter, AbilityType.ACTIVE_OTHERS,
                RolePriority.ROLE_BLOCK, RoleCategory.CORRUPTER_SUPPORT, 0, 0, true);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

        return performAbilityForBlockImmuneRoles(roleOwner, choosenPlayer, gameService);

    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return roleBlock(choosenPlayer);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 10);
    }
}
