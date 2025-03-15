package com.kankangames.shadowofroles.ui.activities.multidevice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.InstanceClearer;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;
import com.kankangames.shadowofroles.ui.activities.ImageChangingActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.adapters.LobbyPlayersAdapter;
import com.kankangames.shadowofroles.ui.alerts.GoToMainAlert;

import java.util.ArrayList;
import java.util.List;

public class GameHostingActivity extends ImageChangingActivity {

    private List<LobbyPlayer> playerList;
    private LobbyPlayersAdapter playerAdapter;
    private Server server;
    private Client client;
    private Button startGameBtn;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        RecyclerView playersView = findViewById(R.id.hosting_lobby_recycler_view);
        startGameBtn = findViewById(R.id.hosting_start_game_button);
        Button plusBtn = findViewById(R.id.plusBtn);
        Button minusBtn = findViewById(R.id.minusBtn);
        backgroundImage = findViewById(R.id.backgroundImage);
        changeImage();

        startServer();

        connectToServer();

        if(client.getLobbyPlayers() != null){
            playerList = client.getLobbyPlayers();
        }
        else{
            playerList = new ArrayList<>();
        }

        playerAdapter = new LobbyPlayersAdapter(playerList, this);
        playersView.setAdapter(playerAdapter);
        playersView.setLayoutManager(new LinearLayoutManager(this));

        startGameBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Oyun başlatılıyor...", Toast.LENGTH_SHORT).show();
            server.startGame();
        });

        plusBtn.setOnClickListener(v -> {
            new Thread(()->{
                server.addLobbyAIPlayer();
                server.sendPlayerList();
            }).start();

        });
        minusBtn.setOnClickListener(v -> {
            new Thread(() -> server.kickPlayer(playerAdapter.getSelectedPosition())).start();

        });
        client.setOnGameStartingListener((gameData)->{
            StartGameService startGameService = StartGameService.getInstance();
            startGameService.setGameMode(GameMode.MULTIPLE_DEVICE);
            startGameService.setGameService(gameData);
            Intent intent = new Intent(this,MultipleDeviceGameActivity.class);
            startActivity(intent);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                InstanceClearer.clearInstances();

                Intent intent = new Intent(GameHostingActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }

    private void startServer() {

        server = Server.getInstance();

        server.startServer();
        runOnUiThread(() -> Toast.makeText(this, "Sunucu başlatıldı", Toast.LENGTH_SHORT).show());
    }

    private void connectToServer() {
        client = ClientManager.getInstance().getClient();
        client.setOnJoinedLobbyListener(players-> {
            runOnUiThread(()-> updatePlayerList(players));
        });
        ClientManager.getInstance().setClient(client);
        client.connectToServer("localhost");
    }

    private void updatePlayerList(List<LobbyPlayer> players) {
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



}