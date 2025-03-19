package com.kankangames.shadowofroles.ui.activities.multidevice.lobby;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.networking.server.ServerLobbyManager;
import com.kankangames.shadowofroles.services.StartGameService;

import java.util.List;

public class GameHostingActivity extends AbstractLobbyActivity {

    private Server server;
    private ServerLobbyManager lobbyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startServer();
        super.onCreate(savedInstanceState);

        startGameBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Oyun başlatılıyor...", Toast.LENGTH_SHORT).show();
            server.getServerGameManager().startGame();
        });

        plusBtn.setOnClickListener(v -> {
            new Thread(()->{
                lobbyManager.addLobbyAIPlayer();
                lobbyManager.sendPlayerList();
            }).start();

        });

        minusBtn.setOnClickListener(v -> {
            new Thread(() -> lobbyManager.kickPlayer(playerAdapter.getSelectedPosition())).start();

        });



    }

    private void startServer() {

        server = Server.getInstance();
        server.startServer();
        lobbyManager = server.getServerLobbyManager();
        runOnUiThread(() -> Toast.makeText(this, "Sunucu başlatıldı", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void connectToServer() {

        client.connectToServer("localhost");
    }

    @Override
    protected String getAlertTitle() {
        return "Game will be disbanded";
    }

    @Override
    protected String getAlertMessage() {
        return "Do you want to go to the main menu?";
    }

    @Override
    protected void backPressedAction() {
        lobbyManager.disbandGame();
    }

    @Override
    protected void updatePlayerList(List<LobbyPlayer> players) {
        if (players != null) {
            playerList.clear();
            playerList.addAll(players);
        }

        int playerCount = playerList.size();
        Log.d("Lobby", "Player list updated: " + playerCount);

        StartGameService startGameService = StartGameService.getInstance();
        runOnUiThread(() -> {
            boolean isStartEnabled = playerCount >= startGameService.MIN_PLAYER_COUNT &&
                    playerCount <= startGameService.MAX_PLAYER_COUNT;
            startGameBtn.setEnabled(isStartEnabled);
            Log.d("Lobby", "Start button enabled: " + isStartEnabled);

            playerAdapter.notifyDataSetChanged();
            Log.d("Lobby", "Adapter notifyDataSetChanged called!");
        });
    }

    @Override
    protected void onDestroy() {
        System.out.println("asgydashdasdj.");
        backPressedAction();
        super.onDestroy();
    }
}