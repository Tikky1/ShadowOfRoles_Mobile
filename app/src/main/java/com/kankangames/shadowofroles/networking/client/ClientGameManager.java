package com.kankangames.shadowofroles.networking.client;

import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ChillGuyListener;
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
        boolean isChillGuyHandled = !message.contains("WAITING_CHILLGUY:");
        String json = message.replace("GAME_ENDED:", "")
                .replace("WAITING_CHILLGUY:", "");
        endGameData = GsonProvider.getGson().fromJson(json, EndGameData.class);
        boolean chillGuyExists = endGameData.getFinishGameService().getChillGuyPlayer() != null;

        client.getClientListenerManager().callListener(OnGameEndedListener.class,
                onGameEndedListener -> onGameEndedListener.onGameEnded(endGameData));
        if(isChillGuyHandled || !chillGuyExists){
            client.getClientListenerManager().callListener(ChillGuyListener.class,
                    ChillGuyListener::onHostSentInfo);
        }
    }

    void handleChillGuy(String message) {
        String json = message.replace("CHILLGUY_EXISTS:", "");
        endGameData = GsonProvider.getGson().fromJson(json, EndGameData.class);

        client.getClientListenerManager().callListener(ChillGuyListener.class,
                ChillGuyListener::onHostSentInfo);
    }


    public void sendPlayerInfo(PlayerInfo playerInfo) {
        client.sendObject(playerInfo, PlayerInfo.class, "UPDATE_PLAYER");
    }

    public void sendChillGuyInfo(Boolean didChillGuyWin){
        client.sendObject(didChillGuyWin, Boolean.class,"ChillGuyWinStatus");
    }
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public EndGameData getEndGameData() {
        return endGameData;
    }


}
