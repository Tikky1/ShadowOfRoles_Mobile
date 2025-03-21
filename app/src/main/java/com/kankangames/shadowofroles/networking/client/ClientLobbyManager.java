package com.kankangames.shadowofroles.networking.client;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonobjects.LobbyData;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDisbandedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnKickedFromLobbyListener;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;

public class ClientLobbyManager {
    private final NetworkListenerManager networkListenerManager;
    private final Client client;
    private LobbyData lobbyData;

    ClientLobbyManager(Client client){
        this.client = client;
        networkListenerManager = client.getClientListenerManager();
    }

    void handleGameDisbanded() {
        networkListenerManager.callListener(OnGameDisbandedListener.class,
                OnGameDisbandedListener::onGameDisbanded);
    }


    void updatePlayersList(String message) {
        Gson gson = GsonProvider.getGson();
        String jsonPlayers = message.replace("PLAYERS:", "");
        lobbyData = gson.fromJson(jsonPlayers, LobbyData.class);

        networkListenerManager.callListener(ConnectionListener.class,
                connectionListener -> connectionListener.onConnectionSuccessful(lobbyData));
    }

    void handleKickedFromLobby() {
        client.setConnectionStatus(ConnectionStatus.DISCONNECTED);
        ClientManager.getInstance().setClient(null);
        networkListenerManager.callListener(OnKickedFromLobbyListener.class,
                OnKickedFromLobbyListener::onKickedFromLobby);
        client.disconnect();
        networkListenerManager.resetListeners();

    }

    public LobbyData getLobbyData() {
        return lobbyData;
    }

    public boolean isHost(){
        if(lobbyData.getPlayer().isPresent()){
            return lobbyData.getPlayer().get().isHost();
        }
        return false;
    }
}
