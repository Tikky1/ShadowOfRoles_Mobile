package com.kankangames.shadowofroles.networking.client;

import android.util.Log;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDataReceivedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.services.DataProvider;

public class ClientGameManager {

    private final Client client;

    private DataProvider dataProvider;
    private EndGameData endGameData;

    public ClientGameManager(Client client) {
        this.client = client;
    }


    void handleGameData(String message) {
        String json = message.replace("GAME_DATA:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        client.getClientListenerManager().callListener(OnGameDataReceivedListener.class, onGameDataReceivedListener ->
                onGameDataReceivedListener.onGameDataReceived((GameData) dataProvider));
    }

    void handleGameStarted(String message) {
        String json = message.replace("GAME_STARTED:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        client.getClientListenerManager().callListener(OnGameStartingListener.class,
                onGameStartingListener ->  onGameStartingListener.onGameStarting(dataProvider));
    }

    void handleGameEnded(String message) {
        String json = message.replace("GAME_ENDED:", "");
        endGameData = GsonProvider.getGson().fromJson(json, EndGameData.class);

        client.getClientListenerManager().callListener(OnGameEndedListener.class,
                onGameEndedListener -> onGameEndedListener.onGameEnded(endGameData));
    }

    public void sendPlayerInfo(PlayerInfo playerInfo) {
        client.sendObject(playerInfo, PlayerInfo.class, "UPDATE_PLAYER");
    }
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public EndGameData getEndGameData() {
        return endGameData;
    }
}
