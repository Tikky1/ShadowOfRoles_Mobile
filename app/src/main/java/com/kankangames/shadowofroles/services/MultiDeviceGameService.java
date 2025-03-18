package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;

import java.util.ArrayList;

public final class MultiDeviceGameService extends BaseGameService{
    TurnTimerService turnTimerService;
    public MultiDeviceGameService(ArrayList<Player> players, TurnTimerService.OnTimeChangeListener onTimeChangeListener) {
        super(players);
        turnTimerService = new TurnTimerService(this, onTimeChangeListener);

    }

    public synchronized void updateAllPlayers(PlayerInfo playerInfo){

        Player player = findPlayer(playerInfo.getSenderPlayerNumber());

        if(player!=null){
            player.getRole().setChoosenPlayer(findPlayer(playerInfo.getChosenPlayerNumber()));
            player.getRole().setTemplate(playerInfo.getSenderRole());
        }

        updateAlivePlayers();
    }


    public Player findPlayer(int number){
        for(int i=0; i < allPlayers.size() ;i++){
            if(number == allPlayers.get(i).getNumber()){
                return allPlayers.get(i);
            }
        }
        return null;
    }
}
