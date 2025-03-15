package com.kankangames.shadowofroles.networking.listeners;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;

import java.util.ArrayList;
import java.util.List;

public interface OnJoinedLobbyListener {

    void onJoinedLobby(List<LobbyPlayer> players);
}
