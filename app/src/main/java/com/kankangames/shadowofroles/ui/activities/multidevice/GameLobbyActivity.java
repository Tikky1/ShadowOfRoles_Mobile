package com.kankangames.shadowofroles.ui.activities.multidevice;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.InstanceClearer;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.adapters.LobbyPlayersAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyActivity extends BaseActivity {

    private List<LobbyPlayer> playerList;
    private LobbyPlayersAdapter playerAdapter;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        RecyclerView playersView = findViewById(R.id.hosting_lobby_recycler_view);
        Button startGameBtn = findViewById(R.id.hosting_start_game_button);
        Button plusBtn = findViewById(R.id.plusBtn);
        Button minusBtn = findViewById(R.id.minusBtn);

        plusBtn.setVisibility(GONE);
        minusBtn.setVisibility(GONE);
        startGameBtn.setVisibility(GONE);


        ClientManager clientManager = ClientManager.getInstance();
        client = clientManager.getClient();
        client.setOnJoinedLobbyListener(this::updatePlayerList);
        client.setOnGameStartingListener((gameData)->{
            StartGameService startGameService = StartGameService.getInstance();
            startGameService.setGameMode(GameMode.MULTIPLE_DEVICE);
            startGameService.setGameService(gameData);
            Intent intent = new Intent(this,MultipleDeviceGameActivity.class);
            startActivity(intent);
        });
        client.connectToServer(clientManager.getIp());

        if(client.getLobbyPlayers() != null){
            playerList = client.getLobbyPlayers();
        }
        else{
            playerList = new ArrayList<>();
        }

        playerAdapter = new LobbyPlayersAdapter(playerList, this);
        playersView.setAdapter(playerAdapter);
        playersView.setLayoutManager(new LinearLayoutManager(this));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                InstanceClearer.clearInstances();

                Intent intent = new Intent(GameLobbyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updatePlayerList(List<LobbyPlayer> players) {
        if (players != null) {
            playerList.clear();
            playerList.addAll(players);
        }

        runOnUiThread(() -> {
            playerAdapter.notifyDataSetChanged();
            Log.d("Lobby", "Adapter notifyDataSetChanged called!");
        });
    }
}