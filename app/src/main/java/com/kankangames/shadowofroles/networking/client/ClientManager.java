package com.kankangames.shadowofroles.networking.client;

import android.util.Log;

public final class ClientManager {
    private static ClientManager instance;
    private Client client;
    private String ip;

    private ClientManager() {} // Private constructor

    public static synchronized ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    public static synchronized void resetManager() {
        System.out.println("reset atılıyor 1");
        if (instance != null) {
            System.out.println("reset atılıyor 2");
            if (instance.client != null) {
                System.out.println("reset atılıyor 3");
                try {

                    instance.client.disconnect();
                    System.out.println("reset atılıyor 4");
                    instance.client = null;
                    System.out.println("reset atılıyor 5");
                } catch (Exception e) {

                    Log.e("ResetManager", "Error disconnecting client", e);
                    System.out.println("reset atılıyor 6");
                }
            }
            System.out.println("reset atılıyor 7");
            instance = null;
            System.out.println("reset atılıyor 8");
        }
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
