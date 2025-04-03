package com.kankangames.shadowofroles.game.models.roles.abilities;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.services.BaseGameService;

import java.util.Locale;

public interface AttackAbility extends RoleAbility{

    default AbilityResult attack(Player roleOwner, Player choosenPlayer, BaseGameService gameService, CauseOfDeath causeOfDeath){
        TextManager textManager = TextManager.getInstance();
        if(roleOwner.getRole().getAttack() > choosenPlayer.getRole().getDefence()){

            choosenPlayer.killPlayer(Time.NIGHT, gameService.getTimeService().getDayCount(), causeOfDeath, false);


            String causeOfDeathStr = causeOfDeath.name();
            gameService.getMessageService().sendAbilityMessage(textManager.getTextEnumSuffix(causeOfDeathStr,"kill_message"), roleOwner);
            gameService.getMessageService().sendAbilityAnnouncement(
                    textManager.getTextEnumSuffix(causeOfDeathStr,"kill_announcement")
                    .replace("{playerName}",choosenPlayer.getNameAndNumber())
                    .replace("{roleName}", choosenPlayer.getRole().getTemplate().getName()
                    ));
            return AbilityResult.SUCCESSFUL;
        }
        else{
            gameService.getMessageService().sendAbilityMessage(textManager.getText(R.string.ability_defence), roleOwner);
            return AbilityResult.ATTACK_INSUFFICIENT;
        }
    }
}
