package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.PriorityChangingRole;
import com.kankangames.shadowofroles.models.roles.enums.AbilityType;

import java.util.ArrayList;
import java.util.Comparator;

public final class AbilityService {
    public final int LORE_KEEPER_WINNING_COUNT;
    private final BaseGameService gameService;

    public AbilityService(BaseGameService gameService) {
        this.gameService = gameService;
        int playerCount = gameService.getAllPlayers().size();
        LORE_KEEPER_WINNING_COUNT = playerCount >= 8 ? 3 : 2;
    }


    /**
     *If it is morning, he casts a vote for the selected player and sends a message stating who they voted for.
     *If it's night, it sends a message about who is using your role.
     */
    private void sendAbilityMessages(final Player player){

        final Player chosenPlayer = player.getRole().getChoosenPlayer();

        AbilityType abilityType = player.getRole().getTemplate().getAbilityType();
        if(!(abilityType == AbilityType.PASSIVE || abilityType == AbilityType.NO_ABILITY) && !player.isAIPlayer()){
            if(chosenPlayer!=null){
                gameService.messageService.sendMessage(TextManager.getInstance().getText("ability_used_on")
                                .replace("{playerName}", chosenPlayer.getNameAndNumber())
                        ,player,false, false);
            }
            else{
                gameService.messageService.sendMessage(TextManager.getInstance().getText("ability_did_not_used"), player, false,false);
            }
        }

    }
    /**
     *  Performs all abilities according to role priorities
     */
    public void performAllAbilities(){
        ArrayList<Player> players = gameService.copyAlivePlayers();
        gameService.chooseRandomPlayersForAI(players);

        // If the roles priority changes in each turn changes the priority
        for(Player player: players){
            if (player.getRole().getTemplate() instanceof PriorityChangingRole) {
                PriorityChangingRole priorityChangingRole = (PriorityChangingRole) player.getRole().getTemplate();
                priorityChangingRole.changePriority();
            }
            sendAbilityMessages(player);
        }

        // Sorts the roles according to priority and if priorities are same sorts
        players.sort(Comparator.comparing((Player player) -> player.getRole().getTemplate().getRolePriority().getPriority()).reversed()
                .thenComparing((Player player) -> player.getRole().getTemplate().getId()));

        // Performs the abilities in the sorted list
        for(Player player: players){
            player.getRole().getTemplate().performAbility(player, player.getRole().getChoosenPlayer(), gameService);
        }

        // Send some messages to some roles
        for(Player player: players){
            gameService.messageService.sendSpecificRoleMessages(player);
        }

        // Resets the players' attributes according to their role
        for(Player player: gameService.alivePlayers){
            player.getRole().resetStates();
        }
        gameService.updateAlivePlayers();
    }

}
