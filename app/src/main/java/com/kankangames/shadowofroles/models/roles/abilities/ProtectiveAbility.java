package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.services.BaseGameService;

public interface ProtectiveAbility {

    default AbilityResult heal(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        TextManager textManager = TextManager.getInstance();
        gameService.getMessageService().sendAbilityMessage(textManager.getText("ability_heal"), roleOwner);

        if(choosenPlayer.getRole().getDefence()<1){
            choosenPlayer.getRole().setDefence(1);
        }

        return AbilityResult.SUCCESSFUL;
    }
    
}
