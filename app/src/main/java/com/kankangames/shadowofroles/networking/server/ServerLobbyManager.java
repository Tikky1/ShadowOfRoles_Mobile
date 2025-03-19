package com.kankangames.shadowofroles.networking.server;

import android.util.Log;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.managers.InstanceClearer;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.models.player.properties.LobbyPlayerStatus;
import com.kankangames.shadowofroles.networking.jsonobjects.GsonProvider;

import java.util.ArrayList;
import java.util.List;

public final class ServerLobbyManager {

    private final List<LobbyPlayer> lobbyPlayers = new ArrayList<>();
    private final List<ClientHandler> clients;
    private final Server server;

    public ServerLobbyManager(Server server) {
        this.server = server;
        clients = server.getClients();
    }

    public void addPlayerToList(ClientHandler clientHandler) {
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
                    server.broadcastMessage(message);

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

        server.broadcastMessage(message);

        Log.d("Server", "Sent player list to clients: " + message);
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

    public void removeClient(ClientHandler clientHandler){
        server.removeClient(clientHandler);
        lobbyPlayers.remove(clientHandler.getClientPlayer());
        sendPlayerList();
    }

    public List<LobbyPlayer> getLobbyPlayers() {
        return lobbyPlayers;
    }
}
