package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.player.Player;

import java.util.ArrayList;

public class MultiDeviceGameService extends BaseGameService{
    private TurnTimerService turnTimerService;
    public MultiDeviceGameService(ArrayList<Player> players) {
        super(players);
        turnTimerService = new TurnTimerService(this);
    }
}
