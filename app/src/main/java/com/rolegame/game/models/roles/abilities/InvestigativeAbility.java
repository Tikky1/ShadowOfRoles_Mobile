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

        String message = LanguageManager.getText("Detective","abilityMessage")
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
        gameService.getMessageService().sendAbilityMessage(LanguageManager.getText("Observer","abilityMessage")
                        .replace("{teamName}", choosenPlayer.getRole().getTemplate().getTeam().name()),roleOwner);
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
            message = LanguageManager.getText("Stalker","nobodyMessage");
        }
        else{
            message = LanguageManager.getText("Stalker","visitMessage")
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

        String message = LanguageManager.getText("DarkRevealer","abilityMessage").replace("{roleName}",choosenPlayer.getRole().getTemplate().getName());
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

        Collections.shuffle(players);
        String message;

        if (players.size() >= 2) {
            message = LanguageManager.getText("Darkseer","abilityMessage")
                    .replace("{roleName1}",players.getFirst().getRole().getTemplate().getName())
                    .replace("{roleName2}",players.get(1).getRole().getTemplate().getName());
        }
        else if (players.size()==1) {
            message = LanguageManager.getText("Darkseer","oneLeftMessage")
                    .replace("{roleName}",players.getFirst().getRole().getTemplate().getName());
        }
        else{
            message = LanguageManager.getText("Darkseer","zeroLeftMessage");
        }

        gameService.getMessageService().sendAbilityMessage(message,roleOwner);
        return AbilityResult.SUCCESSFUL;
    }

}
