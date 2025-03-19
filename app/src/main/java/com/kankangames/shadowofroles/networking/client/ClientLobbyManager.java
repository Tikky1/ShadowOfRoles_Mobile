package com.kankangames.shadowofroles.networking.client;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.models.player.properties.LobbyPlayerStatus;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDisbandedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnKickedFromLobbyListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnOtherPlayerJoinListener;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;

import java.util.List;

public class ClientLobbyManager {
    private final NetworkListenerManager networkListenerManager;
    private final Client client;
    private List<LobbyPlayer> lobbyPlayers;

    ClientLobbyManager(Client client){
        this.client = client;
        networkListenerManager = client.getClientListenerManager();
    }

    void handleGameDisbanded() {
        networkListenerManager.callListener(OnGameDisbandedListener.class,
                OnGameDisbandedListener::onGameDisbanded);
    }


    void updatePlayersList(String message) {
        String jsonPlayers = message.replace("PLAYERS:", "");
        lobbyPlayers = GsonProvider.fromJsonList(jsonPlayers, LobbyPlayer.class);

        networkListenerManager.callListener(ConnectionListener.class,
                connectionListener -> connectionListener.onConnectionSuccessful(lobbyPlayers));
    }

    void handleKickedFromLobby() {
        client.setConnectionStatus(ConnectionStatus.DISCONNECTED);
        ClientManager.getInstance().setClient(null);
        networkListenerManager.callListener(OnKickedFromLobbyListener.class,
                OnKickedFromLobbyListener::onKickedFromLobby);
        client.disconnect();
        networkListenerManager.resetListeners();

    }


    void handlePlayerJoined(String message) {
        String newPlayer = message.split(":")[1];
        networkListenerManager.callListener(OnOtherPlayerJoinListener.class,
                onOtherPlayerJoinListener -> onOtherPlayerJoinListener
                        .onOtherPlayerJoin(new LobbyPlayer(
                                newPlayer, false, false, LobbyPlayerStatus.WAITING)));
    }

    public List<LobbyPlayer> getLobbyPlayers() {
        return lobbyPlayers;
    }
}
