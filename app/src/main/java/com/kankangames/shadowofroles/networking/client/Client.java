package com.kankangames.shadowofroles.networking.client;

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
import com.kankangames.shadowofroles.networking.listeners.OnGameDataReceived;
import com.kankangames.shadowofroles.networking.listeners.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.OnGameStartingListener;
import com.kankangames.shadowofroles.networking.listeners.OnJoinedLobbyListener;
import com.kankangames.shadowofroles.networking.listeners.OnOtherPlayerJoinListener;
import com.kankangames.shadowofroles.services.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private static final int UDP_PORT = 5001;
    private static final int SERVER_PORT = 5000;
    private final Map<String,String> discoveredServers = new LinkedTreeMap<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;
    private OnOtherPlayerJoinListener onOtherPlayerJoinListener;
    private OnJoinedLobbyListener onJoinedLobbyListener;
    private OnGameDataReceived onGameDataReceived;
    private OnGameStartingListener onGameStartingListener;
    private OnGameEndedListener onGameEndedListener;
    private final String ip;
    private final String name;
    private DataProvider dataProvider;
    private EndGameData endGameData;
    private List<LobbyPlayer> lobbyPlayers;

    public Client(String name) {
        this.name = name;
        ip = NetworkManager.getIp();
    }

    public void discoverServers() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName("0.0.0.0"))) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());

                    if (message.startsWith("ShadowOfRolesServer:")) {
                        String[] inputArr = message.split(":");
                        String serverIp = inputArr[1];
                        String hostName = inputArr[2];

                        if (!discoveredServers.containsKey(serverIp)) {
                            discoveredServers.put(serverIp, hostName);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("Client", "Server discovery failed", e);
            }
        }).start();
    }

    public void connectToServer(String serverIp) {
        new Thread(() -> {
            try {
                socket = new Socket(serverIp, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("IP_NAME:" + ip + ":" + name);
                connected = true;

                listenForMessages();

            } catch (IOException e) {
                Log.e("Client", "Connection failed", e);
            }
        }).start();
    }

    private void listenForMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                Log.d("Message Received", message);
                handleMessage(message);
            }
        } catch (IOException e) {
            Log.e("Client", "Error reading message", e);
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
        }
    }

    private void updatePlayersList(String message) {
        String jsonPlayers = message.replace("PLAYERS:", "");
        lobbyPlayers = GsonProvider.fromJsonList(jsonPlayers, LobbyPlayer.class);

        if (onJoinedLobbyListener != null) {
            onJoinedLobbyListener.onJoinedLobby(lobbyPlayers);
        }
    }

    private void handlePlayerJoined(String message) {
        String newPlayer = message.split(":")[1];
        if (onOtherPlayerJoinListener != null) {
            onOtherPlayerJoinListener.onOtherPlayerJoin(
                    new LobbyPlayer(newPlayer, false, false, LobbyPlayerStatus.WAITING)
            );
        }
    }


    private void handleGameData(String message) {
        String json = message.replace("GAME_DATA:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        if (onGameDataReceived != null) {
            onGameDataReceived.onGameDataReceived((GameData) dataProvider);
        }
    }

    private void handleGameStarted(String message) {
        String json = message.replace("GAME_STARTED:", "");
        dataProvider = GsonProvider.getGson().fromJson(json, GameData.class);

        if (onGameStartingListener != null) {
            onGameStartingListener.onGameStarting(dataProvider);
        }
    }

    private void handleGameEnded(String message) {
        String json = message.replace("GAME_ENDED:", "");
        endGameData = GsonProvider.getGson().fromJson(json, EndGameData.class);

        if (onGameEndedListener != null) {
            onGameEndedListener.onGameEnded(endGameData);
        }
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
        } catch (IOException e) {
            Log.e("Client", "Error closing socket", e);
        }
    }


    public Map<String,String> getDiscoveredServers() {
        return discoveredServers;
    }

    // Listener setters
    public void setOnPlayerJoinListener(OnOtherPlayerJoinListener onOtherPlayerJoinListener) {
        this.onOtherPlayerJoinListener = onOtherPlayerJoinListener;
    }

    public void setOnJoinedLobbyListener(OnJoinedLobbyListener onJoinedLobbyListener) {
        this.onJoinedLobbyListener = onJoinedLobbyListener;
    }

    public void setOnGameDataReceived(OnGameDataReceived onGameDataReceived) {
        this.onGameDataReceived = onGameDataReceived;
    }

    public void setOnGameStartingListener(OnGameStartingListener onGameStartingListener) {
        this.onGameStartingListener = onGameStartingListener;
    }

    public void setOnGameEndedListener(OnGameEndedListener onGameEndedListener) {
        this.onGameEndedListener = onGameEndedListener;
    }

    // Getters

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
}
