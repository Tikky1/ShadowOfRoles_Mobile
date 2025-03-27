package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.TimePeriod;
import com.kankangames.shadowofroles.models.message.Message;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataProvider {

    Map<TimePeriod, List<Message>> getMessages();
    ArrayList<Player> getAlivePlayers();
    ArrayList<Player> getDeadPlayers();
    BaseTimeService getTimeService();
    Player getCurrentPlayer();
}
