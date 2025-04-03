package com.kankangames.shadowofroles.networking.client;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.networking.jsonutils.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.LobbyDTO;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.networking.listeners.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDisbandedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnKickedFromLobbyListener;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;

public class ClientLobbyManager {
    private final NetworkListenerManager networkListenerManager;
    private final Client client;
    private LobbyDTO lobbyDTO;

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
        lobbyDTO = gson.fromJson(jsonPlayers, LobbyDTO.class);

        networkListenerManager.callListener(ConnectionListener.class,
                connectionListener -> connectionListener.onConnectionSuccessful(lobbyDTO));
    }

    void handleKickedFromLobby() {
        client.setConnectionStatus(ConnectionStatus.DISCONNECTED);
        ClientManager.getInstance().setClient(null);
        networkListenerManager.callListener(OnKickedFromLobbyListener.class,
                OnKickedFromLobbyListener::onKickedFromLobby);
        client.disconnect();
        networkListenerManager.resetListeners();

    }

    public LobbyDTO getLobbyData() {
        return lobbyDTO;
    }

    public boolean isHost(){
        if(lobbyDTO.getPlayer().isPresent()){
            return lobbyDTO.getPlayer().get().isHost();
        }
        return false;
    }
}
