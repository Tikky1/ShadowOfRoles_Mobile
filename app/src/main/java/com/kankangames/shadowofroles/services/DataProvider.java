package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.ArrayList;
import java.util.List;

public interface DataProvider {

    List<Message> getMessages();
    ArrayList<Player> getAlivePlayers();
    ArrayList<Player> getDeadPlayers();
    BaseTimeService getTimeService();
    Player getCurrentPlayer();
}
