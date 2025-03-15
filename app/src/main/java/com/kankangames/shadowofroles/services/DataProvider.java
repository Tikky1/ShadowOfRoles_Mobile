package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public interface DataProvider {

    LinkedList<Message> getMessages();
    ArrayList<Player> getAlivePlayers();
    ArrayList<Player> getDeadPlayers();
    TimeService getTimeService();
    Player getCurrentPlayer();
}
