package com.kankangames.shadowofroles.models.roles.templates.folkroles.support;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.RoleBlockAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class SealMaster extends FolkRole implements RoleBlockAbility {
    public SealMaster() {
        super(RoleID.SealMaster, AbilityType.ACTIVE_OTHERS, RolePriority.ROLE_BLOCK,
                RoleCategory.FOLK_SUPPORT);
        roleProperties.setCanRoleBlock(true)
                .setRoleBlockImmune(true);
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
        return new ChanceProperty(25, 10);
    }
}
