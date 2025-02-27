package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.gamestate.Time;
import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;
import com.rolegame.game.services.GameService;

import java.util.Locale;

public interface AttackAbility {

    default AbilityResult attack(Player roleOwner, Player choosenPlayer, GameService gameService, CauseOfDeath causeOfDeath){
        LanguageManager languageManager = LanguageManager.getInstance();
        if(roleOwner.getAttack() > choosenPlayer.getDefence()){

            choosenPlayer.killPlayer(Time.NIGHT, gameService.getTimeService().getDayCount(), causeOfDeath, false);


            String causeOfDeathStr = causeOfDeath.toString().toLowerCase(Locale.ROOT);
            gameService.getMessageService().sendAbilityMessage(languageManager.getText(causeOfDeathStr+"_kill_message"), roleOwner);
            gameService.getMessageService().sendAbilityAnnouncement(
                    languageManager.getText(causeOfDeathStr+"_kill_announcement")
                    .replace("{playerName}",choosenPlayer.getNameAndNumber())
                    .replace("{roleName}", choosenPlayer.getRole().getTemplate().getName()
                    ));
            return AbilityResult.SUCCESSFUL;
        }
        else{
            gameService.getMessageService().sendAbilityMessage(languageManager.getText("ability_defence"), roleOwner);
            return AbilityResult.ATTACK_INSUFFICIENT;
        }
    }
}
