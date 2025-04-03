package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.RoleBlockAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Interrupter extends CorrupterRole implements RoleBlockAbility {
    public Interrupter() {
        super(RoleID.Interrupter, AbilityType.ACTIVE_OTHERS,
                RolePriority.ROLE_BLOCK, RoleCategory.CORRUPTER_SUPPORT);

        roleProperties.setKnowsTeamMembers(true)
                .setCanRoleBlock(true)
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
        return new ChanceProperty(30, 10);
    }
}
