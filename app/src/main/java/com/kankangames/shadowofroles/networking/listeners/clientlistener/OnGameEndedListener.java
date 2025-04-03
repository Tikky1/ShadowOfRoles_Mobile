package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.EndGameDTO;

public interface OnGameEndedListener extends ClientListener{

    void onGameEnded(EndGameDTO endGameDTO);
}
