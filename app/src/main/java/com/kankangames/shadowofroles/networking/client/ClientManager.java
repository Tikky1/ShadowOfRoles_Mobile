package com.kankangames.shadowofroles.networking.client;

import android.util.Log;

public class ClientManager {
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
        if (instance != null) {
            if (instance.client != null) {
                try {
                    instance.client.disconnect();
                    instance.client = null;
                } catch (Exception e) {

                    Log.e("ResetManager", "Error disconnecting client", e);
                }
            }

            instance = null;
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
