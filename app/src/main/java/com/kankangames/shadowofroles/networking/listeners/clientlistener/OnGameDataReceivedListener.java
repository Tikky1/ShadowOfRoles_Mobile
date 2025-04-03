package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.GameDTO;

public interface OnGameDataReceivedListener extends ClientListener{

    void onGameDataReceived(GameDTO gameDTO);
}
