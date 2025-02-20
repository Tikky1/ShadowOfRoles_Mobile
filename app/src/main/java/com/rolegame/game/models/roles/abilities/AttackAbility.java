package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.gamestate.CauseOfDeath;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;
import com.rolegame.game.services.GameService;

public interface AttackAbility {

    default AbilityResult attack(Player roleOwner, Player choosenPlayer, GameService gameService, CauseOfDeath causeOfDeath){
        LanguageManager languageManager = LanguageManager.getInstance();
        if(roleOwner.getAttack() > choosenPlayer.getDefence()){
            String roleName = roleOwner.getRole().getTemplate().getId().toString();
            choosenPlayer.setAlive(false);
            choosenPlayer.addCauseOfDeath(causeOfDeath);
            gameService.getMessageService().sendAbilityMessage(languageManager.getText(roleName+"_kill_message"), roleOwner);
            gameService.getMessageService().sendAbilityAnnouncement(languageManager.getText(roleName+"_kill_announcement")
                    .replace("{playerName}",choosenPlayer.getName()));
            return AbilityResult.SUCCESSFUL;
        }
        else{
            gameService.getMessageService().sendAbilityMessage(languageManager.getText("ability_defence"), roleOwner);
            return AbilityResult.ATTACK_INSUFFICIENT;
        }
    }
}
