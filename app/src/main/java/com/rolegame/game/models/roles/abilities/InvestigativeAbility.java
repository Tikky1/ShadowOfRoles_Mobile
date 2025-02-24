package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;
import com.rolegame.game.models.roles.templates.RoleTemplate;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.RoleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public interface InvestigativeAbility {

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult detectiveAbility(Player roleOwner, Player choosenPlayer, GameService gameService){
        RoleTemplate randRole = RoleService.getRandomRole(choosenPlayer.getRole().getTemplate());

        boolean firstIsChosen = new Random().nextBoolean();
        String roleName1 = firstIsChosen ? choosenPlayer.getRole().getTemplate().getName() : randRole.getName();
        String roleName2 = firstIsChosen ? randRole.getName() : choosenPlayer.getRole().getTemplate().getName();

        String message = LanguageManager.getInstance().getText("detective_ability_message")
                .replace("{roleName1}", roleName1)
                .replace("{roleName2}", roleName2);

        gameService.getMessageService().sendAbilityMessage(message, roleOwner);

        return AbilityResult.SUCCESSFUL;
    }

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult observerAbility(Player roleOwner, Player choosenPlayer, GameService gameService){
        gameService.getMessageService().sendAbilityMessage(LanguageManager.getInstance().getText("observer_ability_message")
                        .replace("{teamName}", choosenPlayer.getRole().getTemplate().getWinningTeam().getTeam().name()),roleOwner);
        return AbilityResult.SUCCESSFUL;
    }

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult stalkerAbility(Player roleOwner, Player choosenPlayer, GameService gameService){
        String message;
        if(choosenPlayer.getRole().getChoosenPlayer()==null||!choosenPlayer.getRole().isCanPerform()){
            message = LanguageManager.getInstance().getText("stalker_ability_message_nobody");
        }
        else{
            message = LanguageManager.getInstance().getText("stalker_ability_message")
                    .replace("{playerName}", choosenPlayer.getRole().getChoosenPlayer().getName());
        }

        gameService.getMessageService().sendAbilityMessage(message,roleOwner);
        return AbilityResult.SUCCESSFUL;
    }

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult darkRevealerAbility(Player roleOwner, Player choosenPlayer, GameService gameService){

        String message = LanguageManager.getInstance().getText("darkrevealer_ability_message").replace("{roleName}",choosenPlayer.getRole().getTemplate().getName());
        gameService.getMessageService().sendAbilityMessage(message,roleOwner);

        return AbilityResult.SUCCESSFUL;
    }


    /**
     *
     * @param roleOwner
     * @param gameService
     * @return
     */
    default AbilityResult darkSeerAbility(Player roleOwner, GameService gameService){

        ArrayList<Player> players = new ArrayList<>(gameService.getAlivePlayers());
        players.remove(roleOwner);

        Collections.shuffle(players);
        String message;

        if (players.size() >= 2) {
            message = LanguageManager.getInstance().getText("darkseer_ability_message")
                    .replace("{roleName1}",players.get(0).getRole().getTemplate().getName())
                    .replace("{roleName2}",players.get(1).getRole().getTemplate().getName());
        }
        else if (players.size()==1) {
            message = LanguageManager.getInstance().getText("darkseer_ability_message_one_left")
                    .replace("{roleName}",players.get(0).getRole().getTemplate().getName());
        }
        else{
            message = LanguageManager.getInstance().getText("darkseer_ability_message_no_left");
        }

        gameService.getMessageService().sendAbilityMessage(message,roleOwner);
        return AbilityResult.SUCCESSFUL;
    }

}
