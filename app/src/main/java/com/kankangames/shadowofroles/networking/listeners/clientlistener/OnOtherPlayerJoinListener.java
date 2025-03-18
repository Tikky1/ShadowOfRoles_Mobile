package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;

public interface OnOtherPlayerJoinListener extends ClientListener {
    void onOtherPlayerJoin(LobbyPlayer playerName);
}