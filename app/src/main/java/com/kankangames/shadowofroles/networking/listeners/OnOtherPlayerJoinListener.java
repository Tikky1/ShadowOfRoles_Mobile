package com.kankangames.shadowofroles.networking.listeners;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;

public interface OnOtherPlayerJoinListener {
    void onOtherPlayerJoin(LobbyPlayer playerName);
}