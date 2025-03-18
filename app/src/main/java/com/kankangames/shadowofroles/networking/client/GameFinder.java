package com.kankangames.shadowofroles.networking.client;

import static com.kankangames.shadowofroles.networking.NetworkManager.UDP_PORT;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.kankangames.shadowofroles.networking.server.ConnectionStatus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

public class GameFinder {
    private ConnectionStatus connectionStatus;
    private boolean isRunning = false;
    private final Map<String,String> discoveredServers = new LinkedTreeMap<>();
    private DatagramSocket socket;

    GameFinder(ConnectionStatus connectionStatus){
        this.connectionStatus = connectionStatus;
    }

    public void discoverServers() {

        if(isRunning){
            return;
        }

        isRunning = true;
        new Thread(() -> {
            try {
                socket = new DatagramSocket(UDP_PORT, InetAddress.getByName("0.0.0.0"));
                socket.setBroadcast(true);
                byte[] buffer = new byte[1024];

                while (connectionStatus != ConnectionStatus.CONNECTED && connectionStatus != ConnectionStatus.ERROR) {

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(message);
                    if (message.startsWith("ShadowOfRolesServer:")) {
                        String[] inputArr = message.split(":");
                        String serverIp = inputArr[1];
                        String hostName = inputArr[2];

                        if (!discoveredServers.containsKey(serverIp)) {
                            discoveredServers.put(hostName, serverIp);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("Client", "Server discovery failed", e);
                connectionStatus = ConnectionStatus.ERROR;
            }
        }).start();
    }

    public void stopDiscovery() {
        isRunning = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
    }

    public Map<String,String> getDiscoveredServers() {
        return discoveredServers;
    }
}
