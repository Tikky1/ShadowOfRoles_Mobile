package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.properties.Role;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;

import java.util.ArrayList;

public final class StartGameService {

    public final int MAX_PLAYER_COUNT = 10;
    public final int MIN_PLAYER_COUNT = 5;
    private static StartGameService instance;
    private int playerCount = MIN_PLAYER_COUNT;

    private DataProvider gameService;
    private GameMode gameMode;
    private EndGameData endGameData;


    public static StartGameService getInstance(){

        if(instance == null){
            instance = new StartGameService();
        }

        return instance;

    }

    public static void resetInstance(){
        instance = null;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void initializeGameService(ArrayList<Player> players){
        ArrayList<RoleTemplate> roles = RoleService.initializeRoles(playerCount);
        for(int i=0;i<players.size();i++){
            players.get(i).setRole(new Role(roles.get(i)));
        }
        this.gameService = new SingleDeviceGameService(players);
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

    public DataProvider getGameService() {
        return gameService;
    }

    public void setGameService(DataProvider gameService) {
        this.gameService = gameService;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public EndGameData getEndGameData() {
        return endGameData;
    }

    public void setEndGameData(EndGameData endGameData) {
        this.endGameData = endGameData;
    }
}
