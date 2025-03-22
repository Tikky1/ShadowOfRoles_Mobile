package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.WinStatus;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;

import java.util.ArrayList;
import java.util.Optional;

public final class MultiDeviceGameService extends BaseGameService{
    TurnTimerService turnTimerService;
    public MultiDeviceGameService(ArrayList<Player> players, TurnTimerService.OnTimeChangeListener onTimeChangeListener) {
        super(players, new BaseTimeService());
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

    public void setChillGuyWinStatus(boolean winStatus){
        Optional.ofNullable(finishGameService.getChillGuyPlayer()).ifPresent(
                player -> {
                    player.setWinStatus(winStatus ? WinStatus.WON : WinStatus.LOST);
                    if(winStatus){
                        finishGameService.addWinningTeam(WinningTeam.CHILL_GUY);
                    }
                });
    }
}
