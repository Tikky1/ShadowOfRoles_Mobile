package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.models.roles.abilities.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.BaseGameService;

import java.util.List;

public final class LastJoke extends CorrupterRole implements AttackAbility, RoleSpecificValuesChooser {
    private boolean didUsedAbility;
    public LastJoke() {
        super(RoleID.LastJoke, AbilityType.OTHER_THAN_CORRUPTER,
                RolePriority.LAST_JOKE, RoleCategory.CORRUPTER_SUPPORT, 3, 0, true);
        this.didUsedAbility = false;
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return executeAbility(roleOwner,choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        if(!didUsedAbility && !roleOwner.getDeathProperties().isAlive()){
            didUsedAbility = true;

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
        return !didUsedAbility;
    }

    @Override
    public void chooseRoleSpecificValues(List<Player> choosablePlayers) {}


}


