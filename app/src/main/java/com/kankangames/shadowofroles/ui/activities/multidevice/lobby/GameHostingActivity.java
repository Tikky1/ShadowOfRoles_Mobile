package com.kankangames.shadowofroles.ui.activities.multidevice.lobby;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.networking.server.ServerLobbyManager;
import com.kankangames.shadowofroles.services.StartGameService;

import java.util.List;
import java.util.Locale;

public class GameHostingActivity extends AbstractLobbyActivity {

    private Server server;
    private ServerLobbyManager lobbyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startServer();
        super.onCreate(savedInstanceState);

        startGameBtn.setOnClickListener(v -> {
            server.getServerGameManager().startGame();
        });

        plusBtn.setOnClickListener(v -> {
            if(playerList.size() >= StartGameService.getInstance().MAX_PLAYER_COUNT){
                return;
            }
            new Thread(()->{
                lobbyManager.addLobbyAIPlayer();
                lobbyManager.sendPlayerList();
            }).start();

        });

        minusBtn.setOnClickListener(v -> {
            new Thread(() -> {
                lobbyManager.kickPlayer(playerAdapter.getSelectedPosition());
                playerAdapter.setSelectedPosition(playerAdapter.getSelectedPosition()-1);
            }).start();

        });



    }

    private void startServer() {

        server = Server.getInstance();
        server.startServer();
        lobbyManager = server.getServerLobbyManager();
    }

    @Override
    protected void connectToServer() {

        client.connectToServer("localhost");
    }

    @Override
    protected String getAlertTitle() {
        return getString(R.string.game_will_disbanded);
    }

    @Override
    protected String getAlertMessage() {
        return getString(R.string.main_menu_alert);
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

        StartGameService startGameService = StartGameService.getInstance();
        runOnUiThread(() -> {
            boolean isStartEnabled = playerCount >= startGameService.MIN_PLAYER_COUNT &&
                    playerCount <= startGameService.MAX_PLAYER_COUNT;
            startGameBtn.setEnabled(isStartEnabled);
            playerCountText.setText(String.format(Locale.ROOT, getText(R.string.players_count).toString(), playerList.size()));
            playerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onDestroy() {
        backPressedAction();
        super.onDestroy();
    }
}