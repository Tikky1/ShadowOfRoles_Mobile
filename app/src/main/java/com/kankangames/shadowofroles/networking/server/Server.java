package com.kankangames.shadowofroles.networking.server;

import android.util.Log;

import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.listeners.OnOtherPlayerJoinListener;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private static final int PORT = 5000;
    private static final int UDP_PORT = 5001;
    private final List<ClientHandler> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean running = false;
    private OnOtherPlayerJoinListener onOtherPlayerJoinListener;

    public Server(OnOtherPlayerJoinListener onOtherPlayerJoinListener) {
        this.onOtherPlayerJoinListener = onOtherPlayerJoinListener;
    }

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                running = true;

                // UDP Broadcast başlat
                startUdpBroadcast();

                while (running) {
                    Socket clientSocket = serverSocket.accept();

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                    clients.add(clientHandler);
                    String message = "PLAYERS:" + clients.stream().map(ClientHandler::getClientName).collect(Collectors.joining(","));
                    broadcastMessage(message);
                    String lastName = clientHandler.getClientName();

                    if (onOtherPlayerJoinListener != null){
                        onOtherPlayerJoinListener.onOtherPlayerJoin(lastName);
                    }
                    System.out.println(message);

                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).start();
    }

    // UDP Broadcast fonksiyonu
    public void startUdpBroadcast() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                String localIp = NetworkManager.getIp(); // Sunucunun kendi IP'si

                while (running) {
                    String message = "ShadowOfRolesServer:" + localIp;
                    byte[] buffer = message.getBytes();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, UDP_PORT);
                    socket.send(packet);

                    Thread.sleep(3000); // 3 saniyede bir duyuru yap
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void setOnPlayerJoinListener(OnOtherPlayerJoinListener onOtherPlayerJoinListener) {
        this.onOtherPlayerJoinListener = onOtherPlayerJoinListener;
    }
}