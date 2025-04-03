package com.kankangames.shadowofroles.networking.client;

import com.kankangames.shadowofroles.game.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.EndGameDTO;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.GameDTO;
import com.kankangames.shadowofroles.networking.jsonutils.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.PlayerDTO;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ChillGuyListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDataReceivedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnQuitedGameListener;
import com.kankangames.shadowofroles.game.models.DataProvider;

public class ClientGameManager {

    private final Client client;

    private DataProvider dataProvider;
    private EndGameDTO endGameDTO;

    public ClientGameManager(Client client) {
        this.client = client;
    }


    void leaveFromGame(){
        client.sendObject(dataProvider.getCurrentPlayer(), Player.class,"PLAYER_LEFT_GAME");
    }
    void handleGameData(String message) {
        String json = message.replace("GAME_DATA:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameDTO.class);

        client.getClientListenerManager().callListener(OnGameDataReceivedListener.class, onGameDataReceivedListener ->
                onGameDataReceivedListener.onGameDataReceived((GameDTO) dataProvider));
    }

    void handleGameStarted(String message) {
        String json = message.replace("GAME_STARTED:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameDTO.class);

        client.getClientListenerManager().callListener(OnGameStartingListener.class,
                onGameStartingListener ->  onGameStartingListener.onGameStarting(dataProvider));
    }

    void handleGameEnded(String message) {
        boolean isChillGuyHandled = !message.contains("WAITING_CHILLGUY:");
        String json = message.replace("GAME_ENDED:", "")
                .replace("WAITING_CHILLGUY:", "");
        endGameDTO = GsonProvider.getGson().fromJson(json, EndGameDTO.class);
        boolean chillGuyExists = endGameDTO.getFinishGameService().getChillGuyPlayer() != null;

        client.getClientListenerManager().callListener(OnGameEndedListener.class,
                onGameEndedListener -> onGameEndedListener.onGameEnded(endGameDTO));
        if(isChillGuyHandled || !chillGuyExists){
            client.getClientListenerManager().callListener(ChillGuyListener.class,
                    ChillGuyListener::onHostSentInfo);
        }
    }

    void handleChillGuy(String message) {
        String json = message.replace("CHILLGUY_EXISTS:", "");
        endGameDTO = GsonProvider.getGson().fromJson(json, EndGameDTO.class);

        client.getClientListenerManager().callListener(ChillGuyListener.class,
                ChillGuyListener::onHostSentInfo);
    }

    public void handlePlayerDisconnected(String message) {
        String json = message.replace("PLAYER_DISCONNECTED:", "");
        LobbyPlayer lobbyPlayer = GsonProvider.getGson().fromJson(json, LobbyPlayer.class);

        if(lobbyPlayer.isHost()){
            client.getClientListenerManager().callListener(OnQuitedGameListener.class,
                    OnQuitedGameListener::onHostQuited);
        }

    }


    public void sendPlayerInfo(final PlayerDTO playerDTO) {
        client.sendObject(playerDTO, PlayerDTO.class, "UPDATE_PLAYER");
    }

    public void sendChillGuyInfo(final Boolean didChillGuyWin){
        client.sendObject(didChillGuyWin, Boolean.class,"ChillGuyWinStatus");
    }
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public EndGameDTO getEndGameData() {
        return endGameDTO;
    }



}
