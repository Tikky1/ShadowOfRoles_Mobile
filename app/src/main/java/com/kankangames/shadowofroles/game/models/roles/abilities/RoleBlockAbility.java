package com.kankangames.shadowofroles.game.models.roles.abilities;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;

public interface RoleBlockAbility extends RoleAbility{

    default AbilityResult roleBlock(Player choosenPlayer) {

        choosenPlayer.getRole().setCanPerform(false);

        return AbilityResult.SUCCESSFUL;
    }
}
