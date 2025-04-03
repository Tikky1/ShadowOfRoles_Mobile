package com.kankangames.shadowofroles.networking.client;

import static com.kankangames.shadowofroles.networking.NetworkManager.PORT;

import android.util.Log;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.GameApplication;
import com.kankangames.shadowofroles.utils.managers.SettingsManager;
import com.kankangames.shadowofroles.game.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.jsonutils.GsonProvider;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.networking.listeners.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public final class Client {

    private final NetworkListenerManager networkListenerManager;
    private final ClientLobbyManager clientLobbyManager;
    private final ClientGameManager clientGameManager;
    private final GameFinder gameFinder;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ConnectionStatus connectionStatus = ConnectionStatus.WAITING;
    private final String ip;
    private final String name;


    public Client() {
        name = SettingsManager.getSettings(GameApplication.getAppContext()).username();
        ip = NetworkManager.getIp();
        networkListenerManager = new NetworkListenerManager();
        gameFinder = new GameFinder(connectionStatus);
        clientLobbyManager = new ClientLobbyManager(this);
        clientGameManager = new ClientGameManager(this);
    }


    public void connectToServer(String serverIp) {
        new Thread(() -> {
            try {
                socket = new Socket(serverIp, PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("IP_NAME:" + ip + ":" + name);
                connectionStatus = ConnectionStatus.CONNECTED;

                listenForMessages();

            } catch (IOException e) {
                Log.e("Client", "Connection failed", e);
                networkListenerManager.callListener(ConnectionListener.class,
                        connectionListener -> connectionListener.onConnectionFailed(e.getMessage()));
            }
        }).start();
    }

    private synchronized void listenForMessages() {
        try {
            String message;
            while (!socket.isClosed()) {
                if ((message = in.readLine()) == null) {
                    break;
                }
                Log.d("Message Received", message);
                handleMessage(message);
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                Log.e("Client", "Error reading message", e);
            }
        }
    }

    private void handleMessage(String message) {

        if (message.startsWith("PLAYERS:")) {
            clientLobbyManager.updatePlayersList(message);
        } else if (message.startsWith("GAME_DATA:")) {
            clientGameManager.handleGameData(message);
        } else if (message.startsWith("GAME_STARTED:")) {
            clientGameManager.handleGameStarted(message);
        } else if (message.startsWith("GAME_ENDED:")) {
            clientGameManager.handleGameEnded(message);
        } else if (message.startsWith("KICKED_FROM_LOBBY")) {
            clientLobbyManager.handleKickedFromLobby();
        } else if (message.startsWith("GAME_DISBANDED")) {
            clientLobbyManager.handleGameDisbanded();
        } else if (message.startsWith("CHILLGUY_EXISTS:")) {
            clientGameManager.handleChillGuy(message);
        } else if (message.startsWith("WAITING_CHILLGUY:")) {
           clientGameManager.handleGameEnded(message);
        } else if (message.startsWith("PLAYER_DISCONNECTED:")) {
            clientGameManager.handlePlayerDisconnected(message);
        }
    }


    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            connectionStatus = ConnectionStatus.DISCONNECTED;
            ClientManager.getInstance().setClient(null);
        } catch (IOException e) {
            Log.e("Client", "Error closing socket", e);
            connectionStatus = ConnectionStatus.ERROR;
        }
    }

    public void leaveFromLobby(){
        if(out==null)
            return;
        System.out.println("çağırıldı");
        out.println("LOBBY_PLAYER_LEFT");
        disconnect();
    }

    void sendMessage(String message){
        if(out==null)
            return;
        out.println(message);
    }

    <T> void sendObject(final T object, Class<T> clazz, String prefix){
        T castedObject = clazz.cast(object);

        new Thread(() -> {
            if (out != null) {
                Gson gson = GsonProvider.getGson();
                String playerJson = gson.toJson(castedObject);
                Log.d("Client", "Sending object: " + playerJson);
                out.println(prefix + ":" + playerJson);
            }
        }).start();
    }

    // Getters
    public String getName() {
        return name;
    }



    public List<LobbyPlayer> getLobbyPlayers() {
        if(clientLobbyManager.getLobbyData() == null){
            return null;
        }
        return clientLobbyManager.getLobbyData().getPlayers();
    }

    public NetworkListenerManager getClientListenerManager() {
        return networkListenerManager;
    }

    public GameFinder getGameFinder() {
        return gameFinder;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public ClientGameManager getClientGameManager() {
        return clientGameManager;
    }

    public ClientLobbyManager getClientLobbyManager() {
        return clientLobbyManager;
    }
}
