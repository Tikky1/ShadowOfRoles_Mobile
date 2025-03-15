package com.kankangames.shadowofroles.ui.activities.multidevice;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListOnlineGamesActivity extends BaseActivity {
    private final ArrayList<String> displayedDeviceNames = new ArrayList<>();
    private final Map<String, String> deviceNameToIpMap = new HashMap<>();
    private ArrayAdapter<String> adapter;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online_games);

        ListView activeGamesView = findViewById(R.id.active_games_recycler_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayedDeviceNames);
        activeGamesView.setAdapter(adapter);

        startServerDiscovery();

        activeGamesView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDevice = displayedDeviceNames.get(position);
            connectToServer(selectedDevice);
        });
    }

    private boolean isDiscoveryRunning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private void startServerDiscovery() {
        client = ClientManager.getInstance().getClient();
        client.discoverServers();

        isDiscoveryRunning = true;

        Runnable discoveryTask = new Runnable() {
            @Override
            public void run() {
                if (!isDiscoveryRunning) return;

                synchronized (deviceNameToIpMap) {
                    deviceNameToIpMap.clear();
                    deviceNameToIpMap.putAll(client.getDiscoveredServers());
                }

                displayedDeviceNames.clear();
                displayedDeviceNames.addAll(deviceNameToIpMap.keySet());

                runOnUiThread(() -> adapter.notifyDataSetChanged());

                handler.postDelayed(this, 3000);
            }
        };

        handler.post(discoveryTask);
    }

    private void stopServerDiscovery() {
        isDiscoveryRunning = false;
        handler.removeCallbacksAndMessages(null);
    }


    private void connectToServer(String deviceName) {
        String serverIp = deviceNameToIpMap.get(deviceName);
        if (serverIp != null) {
            ClientManager clientManager = ClientManager.getInstance();
            clientManager.setIp(serverIp);
            Intent intent = new Intent(this,GameLobbyActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sunucuya bağlanılıyor: " + serverIp, LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sunucu bulunamadı!", LENGTH_SHORT).show();
        }
    }
}
