package com.kankangames.shadowofroles.game.models.roles.abilities;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public interface ProtectiveAbility extends RoleAbility{

    default AbilityResult heal(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        TextManager textManager = TextManager.getInstance();
        gameService.getMessageService().sendAbilityMessage(textManager.getText(R.string.ability_heal), roleOwner);

        if(choosenPlayer.getRole().getDefence()<1){
            choosenPlayer.getRole().setDefence(1);
        }

        return AbilityResult.SUCCESSFUL;
    }
    
}
