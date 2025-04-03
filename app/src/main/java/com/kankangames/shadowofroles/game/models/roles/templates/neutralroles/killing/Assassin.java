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
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Assassin extends NeutralRole implements AttackAbility {
    public Assassin() {
        super(RoleID.Assassin, AbilityType.ACTIVE_OTHERS, RolePriority.NONE,
                RoleCategory.NEUTRAL_KILLING, WinningTeam.ASSASSIN);

        roleProperties.setAttack(1)
                .setDefence(1)
                .setHasAttackAbility(true)
                .setWinsAlone(true)
                .setMustBeLastStanding(true)
                .setRoleBlockImmune(true)
                .setCanKill1v1(true);
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

