package com.kankangames.shadowofroles.models.roles.templates.folkroles.analyst;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.InvestigativeAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class Stalker extends FolkRole implements InvestigativeAbility {

    public Stalker() {
        super(RoleID.Stalker, AbilityType.ACTIVE_OTHERS,
                RolePriority.NONE, RoleCategory.FOLK_ANALYST, 0, 0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return stalkerAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25, 10);
    }
}
