package com.kankangames.shadowofroles.networking.client;

import android.os.Build;

import com.kankangames.shadowofroles.networking.listeners.OnPlayerJoinListener;

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
    private OnPlayerJoinListener onPlayerJoinListener;

    public void discoverServers() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(UDP_PORT, InetAddress.getByName("0.0.0.0"))) {
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    if (message.startsWith("SERVER:")) {
                        String serverIp = message.split(":")[1];

                        if (!discoveredServers.contains(serverIp)) {
                            discoveredServers.add(serverIp);
                            System.out.println("Bulunan Sunucu: " + serverIp);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void connectToServer(String serverIp, String playerName) {
        new Thread(() -> {
            try {
                socket = new Socket(serverIp, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(playerName);
                connected = true;
                System.out.println("Sunucuya bağlandınız: " + serverIp);

                // Sunucudan gelen mesajları dinle
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Sunucu: " + message);

                    // Eğer mesaj "katıldı" içeriyorsa yeni oyuncu eklenmiştir
                    if (message.contains("katıldı!")) {
                        String newPlayer = message.replace(" katıldı!", "").trim();
                        if (onPlayerJoinListener != null) {
                            onPlayerJoinListener.onPlayerJoin(newPlayer);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public List<String> getDiscoveredServers() {
        return discoveredServers;
    }

    public void setOnPlayerJoinListener(OnPlayerJoinListener onPlayerJoinListener) {
        this.onPlayerJoinListener = onPlayerJoinListener;
    }
}