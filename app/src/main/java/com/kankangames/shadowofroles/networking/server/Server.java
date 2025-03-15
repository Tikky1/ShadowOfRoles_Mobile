package com.kankangames.shadowofroles.networking.server;

import android.util.Log;

import com.google.gson.Gson;
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

public class Server implements TurnTimerService.OnTimeChangeListener {
    private static Server instance;
    private static final int PORT = 5000;
    private static final int UDP_PORT = 5001;
    private final List<ClientHandler> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean running = false;
    protected MultiDeviceGameService multiDeviceGameService;
    private boolean isGameStarted = false;
    private final List<LobbyPlayer> lobbyPlayers = new ArrayList<>();

    public static Server getInstance(){
        if(instance == null){
            instance = new Server();
        }

        return instance;
    }

    private Server() {}

    public void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
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

    private void addPlayerToList(ClientHandler clientHandler){
        new Thread(() -> {
            try {
                Thread.sleep(200); // 200ms gecikme
                clients.add(clientHandler);
                lobbyPlayers.add(clientHandler.getClientPlayer());

                Gson gson = GsonProvider.getGson();
                String json = gson.toJson(lobbyPlayers);

                String message = "PLAYERS:" + json;
                broadcastMessage(message);

                Log.d("Server", message);
            } catch (InterruptedException e) {
                e.fillInStackTrace();
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
            System.out.println("x");
            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                String localIp = NetworkManager.getIp();
                System.out.println("u");

                while (running && !isGameStarted) {
                    System.out.println("tried");
                    Thread.sleep(3000);
                    String message = "ShadowOfRolesServer:" + localIp + ":" + clients.get(0).getClientPlayer().getName();
                    byte[] buffer = message.getBytes();

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, UDP_PORT);
                    socket.send(packet);
                    System.out.println(message);

                }
            } catch (IOException | InterruptedException e) {
                e.fillInStackTrace();
            }
        }).start();
    }

    public void startGame() {
        ArrayList<Player> players = new ArrayList<>();
        int aiPlayerNum = 1;
        int humanPlayerCount = 0;
        for(int i=0; i < lobbyPlayers.size() ;i++){
            LobbyPlayer lobbyPlayer = lobbyPlayers.get(i);
            if(lobbyPlayer.isAI()){
                players.add(new AIPlayer(i+1, "Bot " + aiPlayerNum));
                aiPlayerNum++;
            }
            else{
                players.add(new HumanPlayer(i+1, clients.get(humanPlayerCount).getClientPlayer().getName()));
                humanPlayerCount++;
            }
        }

        isGameStarted = true;
        multiDeviceGameService = new MultiDeviceGameService(players, this);

        ArrayList<RoleTemplate> roleTemplates = RoleService.initializeRoles(players.size());
        for(int i=0;i<players.size();i++){
            players.get(i).setRole(new Role(roleTemplates.get(i)));
        }
        new Thread( ()->{
            sendGameData(false);
        }).start();

    }

    private void sendGameData(boolean didGameStarted){

        Gson gson = GsonProvider.getGson();
        boolean isGameEnded = multiDeviceGameService.getFinishGameService().isGameFinished();

        if(isGameEnded){
            for(Player player: multiDeviceGameService.getAllPlayers()){
                player.getRole().setChoosenPlayer(null);
            }
            EndGameData endGameData = new EndGameData(
                    multiDeviceGameService.getFinishGameService(),
                    multiDeviceGameService.getAllPlayers()
            );
            String jsonEndGameData = gson.toJson(endGameData, EndGameData.class);
            broadcastMessage("GAME_ENDED:" + jsonEndGameData);
        }
        else{
            int humanPlayerCount = 0;
            for (int i=0; i < lobbyPlayers.size(); i++){
                if(lobbyPlayers.get(i).isAI()){
                    continue;
                }
                GameData gameData = new GameData(
                        multiDeviceGameService.getMessageService().getMessages(),
                        multiDeviceGameService.getDeadPlayers(),
                        multiDeviceGameService.getAlivePlayers(),
                        multiDeviceGameService.getTimeService(),
                        multiDeviceGameService.getFinishGameService().isGameFinished(),
                        i+1
                );


                String jsonGameData = gson.toJson(gameData);
                String message = (didGameStarted ? "GAME_DATA:" : "GAME_STARTED:" ) + jsonGameData;
                clients.get(humanPlayerCount).sendMessage(message);
                humanPlayerCount++;
            }
        }


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
        LobbyPlayer aiPlayer = new LobbyPlayer("Bot",false,true, LobbyPlayerStatus.READY);
        lobbyPlayers.add(aiPlayer);
    }

    public void kickPlayer(int index){

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
        removeClient(clients.get(clientIndex));
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        lobbyPlayers.remove(clientHandler.getClientPlayer());
        clientHandler.closeConnection();
        sendPlayerList();
    }



    @Override
    public void onTimeChange() {
        sendGameData(true);
    }
}
