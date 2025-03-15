package com.kankangames.shadowofroles.networking.listeners;

import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;

public interface OnGameEndedListener {

    void onGameEnded(EndGameData endGameData);
}
