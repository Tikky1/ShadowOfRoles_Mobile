package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;

public interface OnGameEndedListener extends ClientListener{

    void onGameEnded(EndGameData endGameData);
}
