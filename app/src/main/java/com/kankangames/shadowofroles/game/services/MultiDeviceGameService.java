package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.models.gamestate.WinStatus;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.PlayerDTO;

import java.util.ArrayList;
import java.util.Optional;

public final class MultiDeviceGameService extends BaseGameService{
    TurnTimerService turnTimerService;
    public MultiDeviceGameService(ArrayList<Player> players, TurnTimerService.OnTimeChangeListener onTimeChangeListener) {
        super(players, new BaseTimeService());
        turnTimerService = new TurnTimerService(this, onTimeChangeListener);

    }

    public synchronized void updateAllPlayers(PlayerDTO playerDTO){

        Player player = findPlayer(playerDTO.getSenderPlayerNumber());

        if(player!=null){
            player.getRole().setChoosenPlayer(findPlayer(playerDTO.getChosenPlayerNumber()));
            player.getRole().setTemplate(playerDTO.getSenderRole());
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
