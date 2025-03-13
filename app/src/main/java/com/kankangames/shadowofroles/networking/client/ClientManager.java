package com.kankangames.shadowofroles.networking.client;

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
