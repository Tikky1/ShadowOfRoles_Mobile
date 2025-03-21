package com.kankangames.shadowofroles.networking.server;

import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.NetworkListenerManager;

import java.io.*;
import java.net.*;
import java.util.*;

public final class Server {
    private static Server instance;

    private final List<ClientHandler> clients = new ArrayList<>();

    private final ServerLobbyManager serverLobbyManager;
    private final ServerGameManager serverGameManager;
    private final ServerBroadcaster broadcaster;
    private final NetworkListenerManager listenerManager;
    private ServerSocket serverSocket;
    private boolean running = false;

    public static Server getInstance(){
        if(instance == null){
            instance = new Server();
        }

        return instance;
    }

    private Server() {
        serverLobbyManager = new ServerLobbyManager(this);
        serverGameManager = new ServerGameManager(this);
        broadcaster = new ServerBroadcaster(this);
        listenerManager = new NetworkListenerManager();

    }

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(NetworkManager.PORT);
                running = true;

                broadcaster.startUdpBroadcast();

                while (running) {

                    Socket clientSocket = serverSocket.accept();

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this, clients.isEmpty());

                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();

                    serverLobbyManager.addPlayerToList(clientHandler);


                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).start();
    }


    public static void stopServer() {

        if(instance == null){
            return;
        }

        try{
            instance.running = false;

            for (ClientHandler client : instance.clients) {
                client.closeConnection();
            }
            instance.clients.clear();

            if (instance.serverSocket != null) {
                instance.serverSocket.close();
            }
            instance = null;
        }
        catch (IOException e) {
            e.fillInStackTrace();
        }

    }

    public void broadcastMessage(String message) {
        System.out.println("mesaj gönderildi: " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        clientHandler.closeConnection();

    }


    public List<ClientHandler> getClients() {
        return clients;
    }

    public ServerGameManager getServerGameManager() {
        return serverGameManager;
    }

    public ServerLobbyManager getServerLobbyManager() {
        return serverLobbyManager;
    }

    public NetworkListenerManager getListenerManager() {
        return listenerManager;
    }

    public ClientHandler getHost(){
        return clients.get(0);
    }

    public boolean isRunning() {
        return running;
    }
}
