package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.BaseGameService;

import java.util.List;

public final class LastJoke extends RoleTemplate implements AttackAbility, RoleSpecificValuesChooser {

    public LastJoke() {
        super(RoleID.LAST_JOKE, AbilityType.OTHER_THAN_CORRUPTER,
                RolePriority.LAST_JOKE, RoleCategory.CORRUPTER_SUPPORT, WinningTeam.CORRUPTER);

        roleProperties
                .addAttribute(RoleAttribute.KNOWS_TEAM_MEMBERS)
                .addAttribute(RoleAttribute.HAS_POST_DEATH_EFFECT)
                .addAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE)
                .addAttribute(RoleAttribute.HAS_ATTACK_ABILITY)
                .setAbilityUsesLeft(1)
                .setAttack(3);

    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return executeAbility(roleOwner,choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        if(canUseAbility() && !roleOwner.getDeathProperties().isAlive()){

            roleProperties.setAbilityUsesLeft(0);

            if(choosenPlayer==null){
                return AbilityResult.NO_ONE_SELECTED;
            }
            return attack(roleOwner,choosenPlayer, gameService, CauseOfDeath.LAST_JOKE);
        }

        return AbilityResult.NO_ABILITY_USE_LEFT;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(15, 1);
    }

    public boolean canUseAbility() {
        return roleProperties.abilityUsesLeft() > 0;
    }

    @Override
    public void chooseRoleSpecificValues(List<Player> choosablePlayers) {}


}


