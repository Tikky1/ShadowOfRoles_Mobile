package com.kankangames.shadowofroles.networking.client;

import static com.kankangames.shadowofroles.networking.NetworkManager.PORT;
import static com.kankangames.shadowofroles.networking.NetworkManager.UDP_PORT;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.models.player.properties.LobbyPlayerStatus;
import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDataReceivedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDisbandedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnKickedFromLobbyListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnOtherPlayerJoinListener;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;
import com.kankangames.shadowofroles.services.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public final class Client {

    private final Map<String,String> discoveredServers = new LinkedTreeMap<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ConnectionStatus connectionStatus = ConnectionStatus.WAITING;
    private final ClientListenerManager clientListenerManager;
    private final String ip;
    private final String name;
    private DataProvider dataProvider;
    private EndGameData endGameData;
    private List<LobbyPlayer> lobbyPlayers;

    public Client(String name) {
        this.name = name;
        ip = NetworkManager.getIp();
        clientListenerManager = new ClientListenerManager();
    }

    public void discoverServers() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName("0.0.0.0"))) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                while (connectionStatus != ConnectionStatus.CONNECTED && connectionStatus != ConnectionStatus.ERROR) {

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(message);
                    if (message.startsWith("ShadowOfRolesServer:")) {
                        String[] inputArr = message.split(":");
                        String serverIp = inputArr[1];
                        String hostName = inputArr[2];

                        if (!discoveredServers.containsKey(serverIp)) {
                            discoveredServers.put(hostName, serverIp);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("Client", "Server discovery failed", e);
                connectionStatus = ConnectionStatus.ERROR;
            }
        }).start();
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
                clientListenerManager.callListener(ConnectionListener.class,
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
            updatePlayersList(message);
        } else if (message.contains("PLAYER_JOINED:")) {
            handlePlayerJoined(message);
        } else if (message.startsWith("GAME_DATA:")) {
            handleGameData(message);
        } else if (message.startsWith("GAME_STARTED:")) {
            handleGameStarted(message);
        } else if (message.startsWith("GAME_ENDED:")) {
            handleGameEnded(message);
        } else if (message.startsWith("KICKED_FROM_LOBBY")) {
            handleKickedFromLobby();
        } else if (message.startsWith("GAME_DISBANDED")) {
            handleGameDisbanded();
        }
    }

    private void handleGameDisbanded() {
        clientListenerManager.callListener(OnGameDisbandedListener.class,
                OnGameDisbandedListener::onGameDisbanded);
    }


    private void updatePlayersList(String message) {
        String jsonPlayers = message.replace("PLAYERS:", "");
        lobbyPlayers = GsonProvider.fromJsonList(jsonPlayers, LobbyPlayer.class);

        clientListenerManager.callListener(ConnectionListener.class,
                connectionListener -> connectionListener.onConnectionSuccessful(lobbyPlayers));
    }

    private void handleKickedFromLobby() {
        connectionStatus = ConnectionStatus.DISCONNECTED;
        ClientManager.getInstance().setClient(null);
        clientListenerManager.callListener(OnKickedFromLobbyListener.class,
                OnKickedFromLobbyListener::onKickedFromLobby);
        this.disconnect();
        clientListenerManager.resetListeners();

    }


    private void handlePlayerJoined(String message) {
        String newPlayer = message.split(":")[1];
        clientListenerManager.callListener(OnOtherPlayerJoinListener.class,
                onOtherPlayerJoinListener -> onOtherPlayerJoinListener
                        .onOtherPlayerJoin(new LobbyPlayer(
                                newPlayer, false, false, LobbyPlayerStatus.WAITING)));
    }


    private void handleGameData(String message) {
        String json = message.replace("GAME_DATA:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        clientListenerManager.callListener(OnGameDataReceivedListener.class,onGameDataReceivedListener ->
                onGameDataReceivedListener.onGameDataReceived((GameData) dataProvider));
    }

    private void handleGameStarted(String message) {
        String json = message.replace("GAME_STARTED:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        clientListenerManager.callListener(OnGameStartingListener.class,
               onGameStartingListener ->  onGameStartingListener.onGameStarting(dataProvider));
    }

    private void handleGameEnded(String message) {
        String json = message.replace("GAME_ENDED:", "");
        endGameData = GsonProvider.getGson().fromJson(json, EndGameData.class);

        clientListenerManager.callListener(OnGameEndedListener.class,
               onGameEndedListener -> onGameEndedListener.onGameEnded(endGameData));
    }

    public void sendPlayerInfo(PlayerInfo playerInfo) {
        new Thread(() -> {
            if (out != null) {
                Gson gson = GsonProvider.getGson();
                String playerJson = gson.toJson(playerInfo);
                Log.d("Client", "Sending player info: " + playerJson);
                out.println("UPDATE_PLAYER:" + playerJson);
            }
        }).start();
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
        out.println("LOBBY_PLAYER_LEFT");
        disconnect();
    }

    // Getters
    public Map<String,String> getDiscoveredServers() {
        return discoveredServers;
    }

    public String getName() {
        return name;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public EndGameData getEndGameData() {
        return endGameData;
    }

    public List<LobbyPlayer> getLobbyPlayers() {
        return lobbyPlayers;
    }

    public ClientListenerManager getClientListenerManager() {
        return clientListenerManager;
    }
}
