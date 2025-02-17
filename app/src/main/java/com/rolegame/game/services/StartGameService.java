package com.rolegame.game.services;

import com.rolegame.game.models.player.Player;

import java.util.ArrayList;

public class StartGameService {

    public final int MAX_PLAYER_COUNT = 10;
    public final int MIN_PLAYER_COUNT = 5;
    private static StartGameService instance;
    private int playerCount = MIN_PLAYER_COUNT;

    private ArrayList<Player> players;

    public static StartGameService getInstance(){

        if(instance == null){
            instance = new StartGameService();
        }

        return instance;

    }

    public int getPlayerCount() {
        return playerCount;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }


    public int increasePlayerCount(){
        if(playerCount<MAX_PLAYER_COUNT){
            playerCount++;
        }
        return playerCount;
    }

    public int decreasePlayerCount(){
        if(playerCount>MIN_PLAYER_COUNT){
            playerCount--;
        }
        return playerCount;
    }
}
