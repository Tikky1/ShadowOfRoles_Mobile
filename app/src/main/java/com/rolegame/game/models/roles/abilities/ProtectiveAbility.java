package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;
import com.rolegame.game.services.GameService;

public interface ProtectiveAbility {

    default AbilityResult heal(Player roleOwner, Player choosenPlayer, GameService gameService) {
        gameService.getMessageService().sendAbilityMessage(LanguageManager.getText("Ability","heal"), roleOwner);

        if(choosenPlayer.getDefence()<1){
            choosenPlayer.setDefence(1);
        }

        return AbilityResult.SUCCESSFUL;
    }
    
}
