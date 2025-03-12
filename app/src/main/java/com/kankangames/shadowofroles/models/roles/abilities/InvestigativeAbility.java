package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.services.BaseGameService;
import com.kankangames.shadowofroles.services.RoleService;

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
    default AbilityResult detectiveAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){
        RoleTemplate randRole = RoleService.getRandomRole(choosenPlayer.getRole().getTemplate());

        boolean firstIsChosen = new Random().nextBoolean();
        String roleName1 = firstIsChosen ? choosenPlayer.getRole().getTemplate().getName() : randRole.getName();
        String roleName2 = firstIsChosen ? randRole.getName() : choosenPlayer.getRole().getTemplate().getName();

        String message = TextManager.getInstance().getText("detective_ability_message")
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
    default AbilityResult observerAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){
        gameService.getMessageService().sendAbilityMessage(TextManager.getInstance().getText("observer_ability_message")
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
    default AbilityResult stalkerAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){
        String message;
        if(choosenPlayer.getRole().getChoosenPlayer()==null||!choosenPlayer.getRole().isCanPerform()){
            message = TextManager.getInstance().getText("stalker_ability_message_nobody");
        }
        else{
            message = TextManager.getInstance().getText("stalker_ability_message")
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
    default AbilityResult darkRevealerAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){

        String message = TextManager.getInstance().getText("darkrevealer_ability_message").replace("{roleName}",choosenPlayer.getRole().getTemplate().getName());
        gameService.getMessageService().sendAbilityMessage(message,roleOwner);

        return AbilityResult.SUCCESSFUL;
    }


    /**
     *
     * @param roleOwner
     * @param gameService
     * @return
     */
    default AbilityResult darkSeerAbility(Player roleOwner, BaseGameService gameService){

        ArrayList<Player> players = new ArrayList<>(gameService.getAlivePlayers());
        players.remove(roleOwner);

        Collections.shuffle(players);
        String message;

        if (players.size() >= 2) {
            message = TextManager.getInstance().getText("darkseer_ability_message")
                    .replace("{roleName1}",players.get(0).getRole().getTemplate().getName())
                    .replace("{roleName2}",players.get(1).getRole().getTemplate().getName());
        }
        else if (players.size()==1) {
            message = TextManager.getInstance().getText("darkseer_ability_message_one_left")
                    .replace("{roleName}",players.get(0).getRole().getTemplate().getName());
        }
        else{
            message = TextManager.getInstance().getText("darkseer_ability_message_no_left");
        }

        gameService.getMessageService().sendAbilityMessage(message,roleOwner);
        return AbilityResult.SUCCESSFUL;
    }

}
