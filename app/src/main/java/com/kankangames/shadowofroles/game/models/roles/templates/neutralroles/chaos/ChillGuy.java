package com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.chaos;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.NoAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.game.services.BaseGameService;


public final class ChillGuy extends NeutralRole implements NoAbility {
    public ChillGuy() {
        super(RoleID.ChillGuy, AbilityType.NO_ABILITY, RolePriority.NONE, RoleCategory.NEUTRAL_CHAOS,
                WinningTeam.CHILL_GUY);

        roleProperties.setCanWinWithAnyTeam(true)
                .setHasNormalWinCondition(false)
                .setWinsAlone(true);

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
        return new ChanceProperty(10, 1);
    }


}
