package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.analyst;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.InvestigativeAbility;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.GameService;

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
