package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.networking.jsonobjects.GameData;

public interface OnGameDataReceivedListener extends ClientListener{

    void onGameDataReceived(GameData gameData);
}
