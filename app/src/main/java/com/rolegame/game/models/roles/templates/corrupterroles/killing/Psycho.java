package com.rolegame.game.models.roles.templates.corrupterroles.killing;

import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.templates.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.abilities.AttackAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

public final class Psycho extends CorrupterRole implements AttackAbility {

    public Psycho() {
        super(RoleID.Psycho, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.NONE,
                RoleCategory.CORRUPTER_KILLING, 1,0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {

       return attack(roleOwner,choosenPlayer, gameService, CauseOfDeath.PSYCHO);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(100, 1);
    }
}
