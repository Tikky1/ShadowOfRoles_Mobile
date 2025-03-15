package com.kankangames.shadowofroles.networking.listeners;

import com.kankangames.shadowofroles.networking.jsonobjects.GameData;

public interface OnGameDataReceived {

    void onGameDataReceived(GameData gameData);
}
