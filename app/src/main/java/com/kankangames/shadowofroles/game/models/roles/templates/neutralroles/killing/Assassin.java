package com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.killing;

import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Assassin extends RoleTemplate implements AttackAbility {
    public Assassin() {
        super(RoleID.ASSASSIN, AbilityType.ACTIVE_OTHERS, RolePriority.NONE,
                RoleCategory.NEUTRAL_KILLING, WinningTeam.ASSASSIN);

        roleProperties
                .setAttack(1)
                .setDefence(1)
                .addAttribute(RoleAttribute.HAS_ATTACK_ABILITY)
                .addAttribute(RoleAttribute.WINS_ALONE)
                .addAttribute(RoleAttribute.MUST_BE_LAST_STANDING)
                .addAttribute(RoleAttribute.MUST_SURVIVE_UNTIL_END)
                .addAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE)
                .addAttribute(RoleAttribute.CAN_KILL_1V1);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

       return performAbilityForBlockImmuneRoles(roleOwner, choosenPlayer, gameService);

    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return attack(roleOwner,choosenPlayer, gameService, CauseOfDeath.ASSASSIN);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(40, 1);
    }

}

