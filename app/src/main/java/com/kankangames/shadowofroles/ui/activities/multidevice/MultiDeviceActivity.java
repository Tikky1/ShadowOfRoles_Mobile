package com.kankangames.shadowofroles.ui.activities.multidevice;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.NetworkUtils;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiDeviceActivity extends BaseActivity {
    private final ArrayList<String> displayedDeviceNames = new ArrayList<>(); // ListView için
    private final Map<String, String> deviceNameToIpMap = new HashMap<>(); // Cihaz adı -> IP eşlemesi
    private ArrayAdapter<String> adapter;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_device);

        ListView activeGamesView = findViewById(R.id.active_games_recycler_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayedDeviceNames);
        activeGamesView.setAdapter(adapter);

        Button joinGameBtn = findViewById(R.id.join_game_btn);
        Button hostGameBtn = findViewById(R.id.host_game_btn);
        Button refreshBtn = findViewById(R.id.refreshButton);

        // Server aramayı başlat
        startServerDiscovery();

        joinGameBtn.setOnClickListener(v -> {
            if (!displayedDeviceNames.isEmpty()) {
                String selectedDevice = displayedDeviceNames.get(0); // İlk sunucuya bağlan
                connectToServer(selectedDevice);
            } else {
                Toast.makeText(this, "Bağlanılacak sunucu bulunamadı!", LENGTH_SHORT).show();
            }
        });

        hostGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameHostingActivity.class);
            startActivity(intent);
        });

        refreshBtn.setOnClickListener(v -> {
            displayedDeviceNames.clear();
            deviceNameToIpMap.clear();
            adapter.notifyDataSetChanged();
            startServerDiscovery();
            Toast.makeText(this, "Ağ taranıyor...", LENGTH_SHORT).show();
        });

        activeGamesView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDevice = displayedDeviceNames.get(position);
            connectToServer(selectedDevice);
        });
    }

    private void startServerDiscovery() {
        client = new Client();
        client.discoverServers();

        new Thread(() -> {
            while (true) {
                List<String> discoveredIps = client.getDiscoveredServers();
                Map<String, String> nameToIpTempMap = new HashMap<>();
                List<String> deviceNamesTempList = new ArrayList<>();

                for (String ip : discoveredIps) {
                    String deviceName = getDeviceName(ip); // IP'den cihaz adını al
                    nameToIpTempMap.put(deviceName, ip);
                    deviceNamesTempList.add(deviceName);
                }

                runOnUiThread(() -> {
                    displayedDeviceNames.clear();
                    displayedDeviceNames.addAll(deviceNamesTempList);

                    deviceNameToIpMap.clear();
                    deviceNameToIpMap.putAll(nameToIpTempMap);

                    adapter.notifyDataSetChanged();
                });

                try {
                    Thread.sleep(3000); // 3 saniyede bir güncelle
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // IP’den cihaz adını almak için arka planda çalışan fonksiyon
    private String getDeviceName(String ip) {
        try {
            return InetAddress.getByName(ip).getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return ip; // Eğer hata olursa IP’yi göster
        }
    }

    private void connectToServer(String deviceName) {
        String serverIp = deviceNameToIpMap.get(deviceName); // Cihaz adına karşılık gelen IP'yi al
        if (serverIp != null) {
            String playerName = "Player_" + (int) (Math.random() * 100);
            client.connectToServer(serverIp, playerName);
            Toast.makeText(this, "Sunucuya bağlanılıyor: " + serverIp, LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sunucu bulunamadı!", LENGTH_SHORT).show();
        }
    }
}
