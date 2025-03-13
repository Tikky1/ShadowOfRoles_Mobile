package com.kankangames.shadowofroles.ui.activities.multidevice;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyActivity extends BaseActivity {

    private ArrayList<String> playerList;
    private ArrayAdapter<String> playerAdapter;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        ListView playersView = findViewById(R.id.hosting_lobby_recycler_view);
        Button startBtn = findViewById(R.id.hosting_start_game_button);
        startBtn.setVisibility(GONE);
        playerList = new ArrayList<>();
        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        playersView.setAdapter(playerAdapter);

        client = ClientManager.getInstance().getClient();
        //client.requestPlayerList();
        client.setOnJoinedLobbyListener(this::initializePlayerList);
        client.setOnPlayerJoinListener(this::updatePlayerList);
    }

    private void updatePlayerList(String newPlayer) {
        if (!playerList.contains(newPlayer)) {
            playerList.add(newPlayer);
            runOnUiThread(() -> playerAdapter.notifyDataSetChanged());
        }
    }

    private void initializePlayerList(List<String> players){
        playerList = (ArrayList<String>) players;
        runOnUiThread(() ->{
            playerAdapter.notifyDataSetChanged();
        });

    }
}