package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.game.models.message.Message;
import com.kankangames.shadowofroles.game.models.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DataProvider {

    Map<TimePeriod, List<Message>> getMessages();
    ArrayList<Player> getAlivePlayers();
    ArrayList<Player> getDeadPlayers();
    BaseTimeService getTimeService();
    Player getCurrentPlayer();
}
