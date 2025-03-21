package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.jsonobjects.LobbyData;

import java.util.List;

public interface ConnectionListener extends ClientListener {

    void onConnectionSuccessful(LobbyData lobbyData);
    void onConnectionFailed(String errorMessage);
}
