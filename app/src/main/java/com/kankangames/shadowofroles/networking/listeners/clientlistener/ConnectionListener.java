package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;

import java.util.List;

public interface ConnectionListener extends ClientListener {

    void onConnectionSuccessful(List<LobbyPlayer> players);
    void onConnectionFailed(String errorMessage);
}
