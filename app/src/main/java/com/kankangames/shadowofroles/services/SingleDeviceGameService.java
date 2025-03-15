package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.*;

public final class SingleDeviceGameService extends BaseGameService implements DataProvider {

    private Player currentPlayer;
    private int currentPlayerIndex;

    public SingleDeviceGameService(ArrayList<Player> players) {
        super(players);
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


    @Override
    public LinkedList<Message> getMessages() {
        return messageService.getMessages();
    }

    // Getters
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


}
