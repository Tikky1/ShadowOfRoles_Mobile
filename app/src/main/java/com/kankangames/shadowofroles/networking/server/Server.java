package com.kankangames.shadowofroles.networking.server;

import android.util.Log;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.managers.InstanceClearer;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.player.HumanPlayer;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.player.properties.LobbyPlayerStatus;
import com.kankangames.shadowofroles.models.roles.Role;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.networking.NetworkManager;
import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GameData;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;
import com.kankangames.shadowofroles.services.MultiDeviceGameService;
import com.kankangames.shadowofroles.services.RoleService;
import com.kankangames.shadowofroles.services.TurnTimerService;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Server instance;

    private final List<ClientHandler> clients = new ArrayList<>();
    private final List<LobbyPlayer> lobbyPlayers = new ArrayList<>();
    private final ServerGameManager serverGameManager;
    private ServerSocket serverSocket;
    private boolean running = false;

    public static Server getInstance(){
        if(instance == null){
            instance = new Server();
        }

        return instance;
    }

    private Server() {
        serverGameManager = new ServerGameManager(lobbyPlayers, this);
    }

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(NetworkManager.PORT);
                running = true;

                startUdpBroadcast();

                while (running) {

                    Socket clientSocket = serverSocket.accept();

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this, clients.isEmpty());
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();

                    addPlayerToList(clientHandler);


                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }).start();
    }

    private void addPlayerToList(ClientHandler clientHandler) {
        new Thread(() -> {
            int retries = 3;
            long sleepTime = 500;
            while (retries > 0) {
                try {
                    Thread.sleep(sleepTime);
                    sleepTime *= 2;

                    if (clientHandler == null || clientHandler.getConnectionStatus() != ConnectionStatus.CONNECTED) {
                        Log.e("Server", "Client is not connected.");
                        retries--;
                        continue;
                    }

                    clients.add(clientHandler);
                    lobbyPlayers.add(clientHandler.getClientPlayer());

                    Gson gson = GsonProvider.getGson();
                    String json = gson.toJson(lobbyPlayers);

                    String message = "PLAYERS:" + json;
                    broadcastMessage(message);

                    Log.d("Server", "Players updated: " + message);
                    break;
                } catch (InterruptedException e) {
                    Log.e("Server", "Thread interrupted while adding player to list: " + e.getMessage());
                    retries--;
                } catch (Exception e) {
                    Log.e("Server", "Error adding player to list: " + e.getMessage());
                    retries--;
                }
            }
        }).start();
    }


    public void sendPlayerList() {

        Gson gson = GsonProvider.getGson();
        String json = gson.toJson(lobbyPlayers);

        String message = "PLAYERS:" + json;

        broadcastMessage(message);

        Log.d("Server", "Sent player list to clients: " + message);
    }


    public void startUdpBroadcast() {
        new Thread(() -> {

            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                String localIp = NetworkManager.getIp();

                while (running && !serverGameManager.isGameStarted) {

                    Thread.sleep(3000);
                    if(!clients.isEmpty()){
                        String message = "ShadowOfRolesServer:" + localIp + ":" + clients.get(0).getClientPlayer().getName();
                        byte[] buffer = message.getBytes();

                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, NetworkManager.UDP_PORT);
                        socket.send(packet);
                        System.out.println(message);
                    }


                }
            } catch (IOException | InterruptedException e) {
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


    public void addLobbyAIPlayer(){
        LobbyPlayer aiPlayer = new LobbyPlayer("Bot",false,true, LobbyPlayerStatus.BOT);
        lobbyPlayers.add(aiPlayer);
    }

    public void kickPlayer(int index){

        if(index >= lobbyPlayers.size() || index<0){
            return;
        }

        LobbyPlayer kickedPlayer = lobbyPlayers.get(index);

        if(kickedPlayer.isAI()){
            kickAIPlayer(kickedPlayer);
        }
        else{
            kickHumanPlayer(kickedPlayer);
        }
    }

    private void kickAIPlayer(LobbyPlayer player){
        lobbyPlayers.remove(player);
        sendPlayerList();
    }

    private void kickHumanPlayer(LobbyPlayer player){

        if(player.isHost()){
            return;
        }
        int clientIndex = -1;
        for(int i=0;i<clients.size();i++){
            if(player.equals(clients.get(i).getClientPlayer())){
                clientIndex = i;
                break;
            }
        }
        ClientHandler clientHandler = clients.get(clientIndex);
        clientHandler.sendMessage("KICKED_FROM_LOBBY");
        removeClient(clientHandler);
    }


    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        lobbyPlayers.remove(clientHandler.getClientPlayer());
        clientHandler.closeConnection();
        sendPlayerList();
    }

    public void disbandGame(){
        new Thread(()->{
            for(ClientHandler clientHandler : clients){
                clientHandler.sendMessage("GAME_DISBANDED");
                clientHandler.closeConnection();
            }
            clients.clear();
            InstanceClearer.clearInstances();
        }).start();

    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public ServerGameManager getServerGameManager() {
        return serverGameManager;
    }
}
