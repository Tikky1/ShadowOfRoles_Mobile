package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.killing;

import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class Psycho extends CorrupterRole implements AttackAbility {

    public Psycho() {
        super(RoleID.Psycho, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.NONE,
                RoleCategory.CORRUPTER_KILLING, 1,0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

       return attack(roleOwner,choosenPlayer, gameService, CauseOfDeath.PSYCHO);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(100, 1);
    }
}
