package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;

public interface RoleBlockAbility {

    default AbilityResult roleBlock(Player choosenPlayer) {

        choosenPlayer.getRole().setCanPerform(false);

        return AbilityResult.SUCCESSFUL;
    }
}
