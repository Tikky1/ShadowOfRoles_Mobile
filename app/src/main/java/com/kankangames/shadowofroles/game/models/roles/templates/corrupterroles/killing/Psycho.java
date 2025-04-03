package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.killing;

import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.game.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Psycho extends CorrupterRole implements AttackAbility {

    public Psycho() {
        super(RoleID.Psycho, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.NONE,
                RoleCategory.CORRUPTER_KILLING);

        roleProperties.setKnowsTeamMembers(true)
                .setAttack(1)
                .setHasAttackAbility(true)
                .setCanKill1v1(true);
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
