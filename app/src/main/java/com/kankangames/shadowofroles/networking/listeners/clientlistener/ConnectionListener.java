package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.LobbyDTO;

public interface ConnectionListener extends ClientListener {

    void onConnectionSuccessful(LobbyDTO lobbyDTO);
    void onConnectionFailed(String errorMessage);
}
