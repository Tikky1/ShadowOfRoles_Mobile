package com.kankangames.shadowofroles.networking.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private static final int UDP_PORT = 5001;
    private final List<ClientHandler> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean running = false;

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                running = true;
                System.out.println("Server başlatıldı. Bekleniyor...");

                // UDP Broadcast başlat
                startUdpBroadcast();

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Yeni istemci bağlandı: " + clientSocket.getInetAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // UDP Broadcast fonksiyonu
    private void startUdpBroadcast() {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                while (running) {
                    String message = "SERVER:" + InetAddress.getLocalHost().getHostAddress();
                    byte[] buffer = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, UDP_PORT);
                    socket.send(packet);
                    System.out.println("Sunucu duyurusu yapıldı: " + message);
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


}