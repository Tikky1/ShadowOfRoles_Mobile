package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.analyst;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.InvestigativeAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Darkseer extends CorrupterRole implements InvestigativeAbility {
    public Darkseer() {
        super(RoleID.Darkseer, AbilityType.PASSIVE,
                RolePriority.NONE, RoleCategory.CORRUPTER_ANALYST);

        roleProperties.setKnowsTeamMembers(true);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return performAbilityForPassiveRoles(roleOwner, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return darkSeerAbility(roleOwner, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(10, 10);
    }
}
