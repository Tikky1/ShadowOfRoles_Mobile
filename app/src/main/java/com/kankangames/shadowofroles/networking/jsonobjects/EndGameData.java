package com.kankangames.shadowofroles.networking.jsonobjects;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.services.FinishGameService;

import java.util.ArrayList;

public class EndGameData {
    private final FinishGameService finishGameService;
    private final ArrayList<Player> allPlayers;

    public EndGameData(FinishGameService finishGameService, ArrayList<Player> allPlayers) {
        this.finishGameService = finishGameService;
        this.allPlayers = allPlayers;
    }

    public FinishGameService getFinishGameService() {
        return finishGameService;
    }

    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }
}
