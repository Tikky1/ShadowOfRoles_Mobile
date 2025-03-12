package com.kankangames.shadowofroles.ui.activities.multidevice;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

import java.util.ArrayList;

public class GameLobbyActivity extends BaseActivity {

    private ArrayList<String> playerList;
    private ArrayAdapter<String> playerAdapter;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        ListView playersView = findViewById(R.id.hosting_lobby_recycler_view);

        playerList = new ArrayList<>();
        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        playersView.setAdapter(playerAdapter);

        client = ClientManager.getInstance().getClient();
        //client.setOnPlayerJoinListener(this::updatePlayerList);
    }

    private void updatePlayerList(String newPlayer) {
        if (!playerList.contains(newPlayer)) {
            playerList.add(newPlayer);
            runOnUiThread(() -> playerAdapter.notifyDataSetChanged());
        }
    }
}