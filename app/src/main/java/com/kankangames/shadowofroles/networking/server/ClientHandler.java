package com.kankangames.shadowofroles.networking.server;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.game.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.player.properties.LobbyPlayerStatus;
import com.kankangames.shadowofroles.networking.jsonutils.GsonProvider;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.PlayerDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public final class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Server server;
    private LobbyPlayer clientPlayer;
    private String clientIp;
    private final boolean isHost;
    private ConnectionStatus connectionStatus = ConnectionStatus.CONNECTED;

    public ClientHandler(Socket socket, Server server, boolean isHost) {
        this.socket = socket;
        this.server = server;
        this.isHost = isHost;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String received = in.readLine();
            if(received.startsWith("IP_NAME:")){
                String[] inArr = received.split(":");
                clientIp = inArr[1];
                clientPlayer = new LobbyPlayer(inArr[2], isHost, false,
                        server.getServerLobbyManager().getLobbyPlayers().size(), isHost ? LobbyPlayerStatus.HOST : LobbyPlayerStatus.WAITING);
            }


            String message;
            while ((message = in.readLine()) != null && connectionStatus == ConnectionStatus.CONNECTED) {
                if(message.startsWith("IP_NAME:")){
                    server.broadcastMessage(clientIp + ": " + message);
                }
                else if(message.startsWith("UPDATE_PLAYER:")){
                    Gson gson = GsonProvider.getGson();
                    String playerJson = message.replace("UPDATE_PLAYER:","");

                    PlayerDTO player = gson.fromJson(playerJson, PlayerDTO.class);
                    server.getServerGameManager().multiDeviceGameService.updateAllPlayers(player);
                }
                else if(message.startsWith("LOBBY_PLAYER_LEFT")){
                    server.getServerLobbyManager().removeClient(this);
                    closeConnection();
                } else if (message.startsWith("ChillGuyWinStatus:")) {
                    Gson gson = GsonProvider.getGson();
                    String boolJson = message.replace("ChillGuyWinStatus:","");
                    Boolean chillGuyWinStatus = gson.fromJson(boolJson, Boolean.class);
                    server.getServerGameManager().multiDeviceGameService.setChillGuyWinStatus(chillGuyWinStatus);
                    server.getServerGameManager().sendGameData(true);
                } else if(message.startsWith("PLAYER_LEFT_GAME:")){
                    Gson gson = GsonProvider.getGson();
                    String json = message.replace("PLAYER_LEFT_GAME:","");
                    Player player = gson.fromJson(json, Player.class);
                    if(isHost){

                    }else{

                    }
                }

            }
        } catch (IOException e) {
            e.fillInStackTrace();
            setConnectionStatus(ConnectionStatus.ERROR);
        } finally {
            try {
                socket.close();
                server.getServerLobbyManager().removeClient(this);
                setConnectionStatus(ConnectionStatus.DISCONNECTED);
            } catch (IOException e) {
                setConnectionStatus(ConnectionStatus.ERROR);
                e.fillInStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                setConnectionStatus(ConnectionStatus.DISCONNECTED);
            }
        } catch (IOException e) {
            setConnectionStatus(ConnectionStatus.ERROR);
            e.fillInStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public LobbyPlayer getClientPlayer() {
        return clientPlayer;
    }

    void setConnectionStatus(ConnectionStatus connectionStatus){
        this.connectionStatus = connectionStatus;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
}