package com.kankangames.shadowofroles.ui.activities.multidevice;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.ImageChangingActivity;
import com.kankangames.shadowofroles.ui.activities.multidevice.lobby.GameLobbyActivity;
import com.kankangames.shadowofroles.ui.adapters.ServersAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListOnlineGamesActivity extends ImageChangingActivity {
    private final ArrayList<String> displayedDeviceNames = new ArrayList<>();
    private final Map<String, String> deviceNameToIpMap = new ConcurrentHashMap<>();
    private ServersAdapter adapter;
    private Client client;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_online_games);

        backgroundImage = findViewById(R.id.backgroundImage);
        changeImage();

        RecyclerView activeGamesView = findViewById(R.id.active_games_recycler_view);
        adapter =  new ServersAdapter(displayedDeviceNames, this::connectToServer);
        activeGamesView.setAdapter(adapter);
        activeGamesView.setLayoutManager(new LinearLayoutManager(this));

        startServerDiscovery();
    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }

    private boolean isDiscoveryRunning = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private void startServerDiscovery() {
        client = ClientManager.getInstance().getClient();
        client.getGameFinder().discoverServers();
        isDiscoveryRunning = true;

        Runnable discoveryTask = new Runnable() {
            @Override
            public void run() {
                if (!isDiscoveryRunning) return;

                synchronized (deviceNameToIpMap) {
                    deviceNameToIpMap.clear();
                    deviceNameToIpMap.putAll(client.getGameFinder().getDiscoveredServers());
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
        client.getGameFinder().stopDiscovery();
        isDiscoveryRunning = false;
        handler.removeCallbacksAndMessages(null);
    }


    private void connectToServer(String deviceName) {
        String serverIp = deviceNameToIpMap.get(deviceName);
        if (serverIp != null) {
            ClientManager clientManager = ClientManager.getInstance();
            clientManager.setIp(serverIp);
            Intent intent = new Intent(this, GameLobbyActivity.class);
            startActivity(intent);
            stopServerDiscovery();
        } else {
            Toast.makeText(this, getString(R.string.error), LENGTH_SHORT).show();
        }
    }
}
