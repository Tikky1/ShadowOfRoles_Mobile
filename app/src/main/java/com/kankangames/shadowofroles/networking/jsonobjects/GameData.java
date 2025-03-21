package com.kankangames.shadowofroles.networking.jsonobjects;

import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.services.BaseTimeService;
import com.kankangames.shadowofroles.services.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class GameData implements DataProvider {
    private final List<Message> messages;
    private final ArrayList<Player> deadPlayers;
    private final ArrayList<Player> alivePlayers;
    private final BaseTimeService timeService;
    private final boolean isGameFinished;
    private final int playerNumber;

    public GameData(List<Message> messages, ArrayList<Player> deadPlayers, ArrayList<Player> alivePlayers, BaseTimeService timeService, boolean isGameFinished, int playerNumber) {
        this.messages = messages;
        this.deadPlayers = deadPlayers;
        this.alivePlayers = alivePlayers;
        this.timeService = timeService;
        this.isGameFinished = isGameFinished;
        this.playerNumber = playerNumber;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public ArrayList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    @Override
    public ArrayList<Player> getAlivePlayers() {
        return alivePlayers;
    }

    @Override
    public BaseTimeService getTimeService() {
        return timeService;
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    @Override
    public Player getCurrentPlayer(){
        for(Player player: alivePlayers){
            if(player.isSamePlayer(playerNumber)){
                return player;
            }
        }

        for(Player player: deadPlayers){
            if(player.isSamePlayer(playerNumber)){
                return player;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "messages=" + messages +
                ", deadPlayers=" + deadPlayers +
                ", alivePlayers=" + alivePlayers +
                ", timeService=" + timeService +
                ", isGameFinished=" + isGameFinished +
                ", playerNumber=" + playerNumber +
                '}';
    }
}
