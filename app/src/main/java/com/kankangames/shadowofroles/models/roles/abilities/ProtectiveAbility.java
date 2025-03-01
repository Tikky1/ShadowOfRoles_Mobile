package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.managers.LanguageManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.services.GameService;

public interface ProtectiveAbility {

    default AbilityResult heal(Player roleOwner, Player choosenPlayer, GameService gameService) {
        LanguageManager languageManager = LanguageManager.getInstance();
        gameService.getMessageService().sendAbilityMessage(languageManager.getText("ability_heal"), roleOwner);

        if(choosenPlayer.getDefence()<1){
            choosenPlayer.setDefence(1);
        }

        return AbilityResult.SUCCESSFUL;
    }
    
}
