package com.kankangames.shadowofroles.models.roles.templates.neutralroles.chaos;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.NoAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.services.BaseGameService;

public final class Clown extends NeutralRole implements NoAbility {
    public Clown() {
        super(RoleID.Clown, AbilityType.NO_ABILITY, RolePriority.NONE, RoleCategory.NEUTRAL_CHAOS,
               WinningTeam.CLOWN);

        roleProperties.setCanWinWithAnyTeam(true)
                .setHasNormalWinCondition(false)
                .setWinsAlone(true)
                .setMustDieToWin(true);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return performAbilityForNoAbilityRoles();
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return doNothing();
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 1);
    }

}
