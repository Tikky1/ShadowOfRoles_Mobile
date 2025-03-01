package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.AbilityResult;

public interface RoleBlockAbility {

    default AbilityResult roleBlock(Player choosenPlayer) {

        choosenPlayer.getRole().setCanPerform(false);

        return AbilityResult.SUCCESSFUL;
    }
}
