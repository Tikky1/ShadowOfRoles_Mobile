package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.*;

public final class SingleDeviceGameService extends BaseGameService{

    private Player currentPlayer;
    private int currentPlayerIndex;

    public SingleDeviceGameService(ArrayList<Player> players) {
        super(players);
    }


    @Override
    public void performAllAbilities() {
        chooseRandomPlayersForAI(alivePlayers);
        super.performAllAbilities();
    }

    /**
     *If it is morning, he casts a vote for the selected player and sends a message stating who they voted for.
     *If it's night, it sends a message about who is using your role.
     */
    public void sendVoteMessages(){

        final Player chosenPlayer = currentPlayer.getRole().getChoosenPlayer();

        if(timeService.getTime() == Time.VOTING){
            votingService.vote(currentPlayer,chosenPlayer);

            if(chosenPlayer!=null){
                messageService.sendMessage(TextManager.getInstance().getText("voted_for")
                                .replace("{playerName}", chosenPlayer.getNameAndNumber())
                        ,currentPlayer,false, true);
            }else{
                messageService.sendMessage(TextManager.getInstance().getText("voted_for_none"), currentPlayer, false, true);
            }

        }
        else if(timeService.getTime() == Time.NIGHT){
            AbilityType abilityType = currentPlayer.getRole().getTemplate().getAbilityType();
            if(!(abilityType == AbilityType.PASSIVE || abilityType == AbilityType.NO_ABILITY)){
                if(chosenPlayer!=null){
                    messageService.sendMessage(TextManager.getInstance().getText("ability_used_on")
                                    .replace("{playerName}", chosenPlayer.getNameAndNumber())
                            ,currentPlayer,false, false);
                }
                else{
                    messageService.sendMessage(TextManager.getInstance().getText("ability_did_not_used"), currentPlayer, false,false);
                }
            }
        }
    }


    @Override
    public void updateAlivePlayers(){

        super.updateAlivePlayers();
        if(!alivePlayers.isEmpty()){
            moveToFirstHumanPlayer();
        }

    }

    /**
     * Passes to the turn to the next player
     * @return true if time state is changed
     */
    public boolean passTurn() {

        if(!doesHumanPlayerExist()){
            while(!finishGameService.checkGameFinished()){
                toggleDayNightCycle();
            }
            finishGameService.finishGame();
            return true;
        }

        boolean firstTurn = true;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % alivePlayers.size();
            currentPlayer = alivePlayers.get(currentPlayerIndex);

            if (currentPlayerIndex == findFirstHumanPlayer()) {
                toggleDayNightCycle();
                firstTurn = false;
            }

        } while (currentPlayer instanceof AIPlayer && firstTurn);
        return !firstTurn;
    }

    /**
     * Moves the turn to the first human player, skipping AI players.
     */
    private void moveToFirstHumanPlayer() {

        for(int i=0;i<alivePlayers.size();i++){
            if(!alivePlayers.get(i).isAIPlayer()){
                currentPlayerIndex = i;
                currentPlayer = alivePlayers.get(currentPlayerIndex);
                break;
            }
        }
    }

    private int findFirstHumanPlayer() {

        for(int i=0;i<alivePlayers.size();i++){
            if(!alivePlayers.get(i).isAIPlayer()){
                return i;
            }
        }
        return -1;
    }

    private boolean doesHumanPlayerExist(){
        for (final Player alivePlayer : alivePlayers) {
            if (!alivePlayer.isAIPlayer()) {
                return true;
            }
        }
        return false;

    }

    private void chooseRandomPlayersForAI(List<Player> players){
        for(Player player: players){
            if(player instanceof AIPlayer){
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.chooseRandomPlayerNight(alivePlayers);
            }
        }
    }

    // Getters
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


}
