package com.kankangames.shadowofroles.networking.server;

import com.kankangames.shadowofroles.networking.NetworkManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public final class ServerBroadcaster {
    private final Server server;
    private final List<ClientHandler> clients;

    public ServerBroadcaster(Server server) {
        this.server = server;
        this.clients = server.getClients();
    }

    public void startUdpBroadcast() {
        new Thread(() -> {

            try (DatagramSocket socket = new DatagramSocket()) {
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                String localIp = NetworkManager.getIp();

                while (server.isRunning() && !server.getServerGameManager().isGameStarted) {

                    Thread.sleep(3000);
                    if(!clients.isEmpty()){
                        String message = "ShadowOfRolesServer:" + localIp + ":" + server.getHost().getClientPlayer().getName();
                        byte[] buffer = message.getBytes();

                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, NetworkManager.UDP_PORT);
                        socket.send(packet);
                        System.out.println(message);
                    }


                }
            } catch (IOException | InterruptedException e) {
                e.fillInStackTrace();
            }
        }).start();
    }
}
