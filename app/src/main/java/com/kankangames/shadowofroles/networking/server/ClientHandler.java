package com.kankangames.shadowofroles.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Server server;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            clientName = in.readLine();
            System.out.println(clientName + " bağlandı.");

            server.broadcastMessage(clientName + " oyuna katıldı!");

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(clientName + ": " + message);
                server.broadcastMessage(clientName + ": " + message);
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String getClientName() {
        if(clientName!=null) return clientName;
        return "Player_" + new Random().nextInt(100);
    }




}