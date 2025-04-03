package com.kankangames.shadowofroles.networking.listeners.clientlistener;

import com.kankangames.shadowofroles.game.models.DataProvider;

public interface OnGameStartingListener extends ClientListener{
    void onGameStarting(DataProvider dataProvider);
}
