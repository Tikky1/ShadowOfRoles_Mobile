package com.rolegame.game.services;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.Role;
import com.rolegame.game.models.roles.templates.RoleTemplate;

import java.util.ArrayList;

public class StartGameService {

    public final int MAX_PLAYER_COUNT = 10;
    public final int MIN_PLAYER_COUNT = 5;
    private static StartGameService instance;
    private int playerCount = MIN_PLAYER_COUNT;

    private GameService gameService;

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

    public void initializeGameService(ArrayList<Player> players){
        ArrayList<RoleTemplate> roles = RoleService.initializeRoles(playerCount);
        for(int i=0;i<players.size();i++){
            players.get(i).setRole(new Role(roles.get(i)));
        }
        this.gameService = new GameService(players);
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

    public void setPlayerCountMin(){
        playerCount = MIN_PLAYER_COUNT;
    }

    public void setPlayerCountMax(){
        playerCount = MAX_PLAYER_COUNT;
    }



    public GameService getGameService() {
        return gameService;
    }
}
