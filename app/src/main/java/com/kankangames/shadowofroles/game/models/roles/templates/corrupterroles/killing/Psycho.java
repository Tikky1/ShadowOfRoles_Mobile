package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.killing;

import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Psycho extends RoleTemplate implements AttackAbility {

    public Psycho() {
        super(RoleID.PSYCHO, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.NONE,
                RoleCategory.CORRUPTER_KILLING, WinningTeam.CORRUPTER);

        roleProperties
                .addAttribute(RoleAttribute.KNOWS_TEAM_MEMBERS)
                .addAttribute(RoleAttribute.HAS_ATTACK_ABILITY)
                .addAttribute(RoleAttribute.CAN_KILL_1V1)
                .setAttack(1);
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
