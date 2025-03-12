package com.kankangames.shadowofroles.ui.activities.multidevice;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

import java.util.ArrayList;

public class GameHostingActivity extends BaseActivity {

    private ArrayList<String> playerList;
    private ArrayAdapter<String> playerAdapter;
    private Server server;
    private Client client;
    private final String playerName = "Host_" + (int) (Math.random() * 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        ListView playersView = findViewById(R.id.hosting_lobby_recycler_view);
        Button startGameBtn = findViewById(R.id.hosting_start_game_button);

        playerList = new ArrayList<>();
        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerList);
        playersView.setAdapter(playerAdapter);

        // Sunucuyu başlat
        startServer();

        // Sunucu cihazını istemci olarak da bağla
        connectToServer("localhost");

        startGameBtn.setOnClickListener(v -> Toast.makeText(this, "Oyun başlatılıyor...", Toast.LENGTH_SHORT).show());
    }

    private void startServer() {
        server = new Server();
        //server.setOnPlayerJoinListener(this::updatePlayerList);
        server.startServer();
        runOnUiThread(() -> Toast.makeText(this, "Sunucu başlatıldı", Toast.LENGTH_SHORT).show());
    }

    private void connectToServer(String serverIp) {
        client = new Client();
        client.connectToServer(serverIp, playerName);
        updatePlayerList(playerName);
    }

    private void updatePlayerList(String newPlayer) {
        if (!playerList.contains(newPlayer)) {
            playerList.add(newPlayer);
            runOnUiThread(() -> playerAdapter.notifyDataSetChanged());
        }
    }
}