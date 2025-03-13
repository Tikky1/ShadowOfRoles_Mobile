package com.kankangames.shadowofroles.networking.client;

import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.listeners.OnJoinedLobbyListener;
import com.kankangames.shadowofroles.networking.listeners.OnOtherPlayerJoinListener;
import com.kankangames.shadowofroles.networking.listeners.OnServerFoundListener;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final int UDP_PORT = 5001;
    private static final int SERVER_PORT = 5000;
    private final List<String> discoveredServers = new ArrayList<>();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;
    private OnOtherPlayerJoinListener onOtherPlayerJoinListener;
    private OnServerFoundListener onServerFoundListener;
    private OnJoinedLobbyListener onJoinedLobbyListener;
    private final String ip;
    private final String name;

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
                        String serverIp = message.split(":")[1];

                        if (!discoveredServers.contains(serverIp)) {
                            discoveredServers.add(serverIp);
                            if (onServerFoundListener != null) onServerFoundListener.onServerFound(serverIp);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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

                // Sunucudan gelen mesajları dinle
                String message;
                while ((message = in.readLine()) != null) {
                    receivePlayersList(message);

                    if (message.contains("PLAYER_JOINED:")) {
                        String newPlayer = message.split(":")[1];
                        if (onOtherPlayerJoinListener != null) onOtherPlayerJoinListener.onOtherPlayerJoin(newPlayer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void requestPlayerList() {
        new Thread(() -> {
            if (out != null) {
                out.println("GET_PLAYERS");
            }
        }).start();
    }

    private void receivePlayersList(String message) {
        if (message.startsWith("PLAYERS:")) {
            String[] playerNames = message.replace("PLAYERS:", "").split(",");

            if (onJoinedLobbyListener != null) {
                onJoinedLobbyListener.onJoinedLobby(List.of(playerNames));
            }
        }
    }

    public List<String> getDiscoveredServers() {
        return discoveredServers;
    }

    public void setOnPlayerJoinListener(OnOtherPlayerJoinListener onOtherPlayerJoinListener) {
        this.onOtherPlayerJoinListener = onOtherPlayerJoinListener;
    }

    public void setOnServerFoundListener(OnServerFoundListener onServerFoundListener) {
        this.onServerFoundListener = onServerFoundListener;
    }

    public void setOnJoinedLobbyListener(OnJoinedLobbyListener onJoinedLobbyListener) {
        this.onJoinedLobbyListener = onJoinedLobbyListener;
    }

    public String getName() {
        return name;
    }
}