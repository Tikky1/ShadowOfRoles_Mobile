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

    public Client() {
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
                    System.out.println("mesaj alındı: " + message);
                    if (message.startsWith("ShadowOfRolesServer:")) {
                        String serverIp = message.split(":")[1];

                        if (!discoveredServers.contains(serverIp)) {
                            discoveredServers.add(serverIp);
                            System.out.println("Bulunan Sunucu: " + serverIp);

                            if(onServerFoundListener !=null) onServerFoundListener.onServerFound(serverIp);
                        }
                    }
                    receivePlayersList(message);

                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).start();
    }

    public void connectToServer(String serverIp, String playerName) {
        new Thread(() -> {
            try {
                socket = new Socket(serverIp, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(ip);
                connected = true;
                System.out.println("Sunucuya bağlandınız: " + serverIp);

                // Sunucudan gelen mesajları dinle
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Sunucu: " + message);

                    // Eğer mesaj "katıldı" içeriyorsa yeni oyuncu eklenmiştir
                    if (message.contains("katıldı!")) {
                        String newPlayer = message.replace(" katıldı!", "").trim();
                        if (onOtherPlayerJoinListener != null) onOtherPlayerJoinListener.onOtherPlayerJoin(newPlayer);
                       // if (onJoinedLobbyListener != null) onJoinedLobbyListener.onJoinedLobby();

                    }
                }

            } catch (IOException e) {
                e.fillInStackTrace();
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

    private void receivePlayersList(String message){
        if (message.startsWith("PLAYERS:")) {
            System.out.println("players received: " + message);
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
}