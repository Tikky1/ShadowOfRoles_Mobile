package com.rolegame.game.models.roles.corrupterroles.support;

import com.rolegame.game.gamestate.CauseOfDeath;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.AttackAbility;
import com.rolegame.game.models.roles.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

public final class LastJoke extends CorrupterRole implements AttackAbility {
    private boolean didUsedAbility;
    public LastJoke() {
        super(RoleID.LastJoke, AbilityType.OTHER_THAN_CORRUPTER,
                RolePriority.LAST_JOKE, RoleCategory.CORRUPTER_SUPPORT, 3, 0, true);
        this.didUsedAbility = false;
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return executeAbility(roleOwner,choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        if(!didUsedAbility && !roleOwner.isAlive()){
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
        return new ChanceProperty(15,1);
    }

    public boolean isDidUsedAbility() {
        return didUsedAbility;
    }
}


