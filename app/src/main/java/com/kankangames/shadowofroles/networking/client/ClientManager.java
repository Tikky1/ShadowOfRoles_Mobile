package com.kankangames.shadowofroles.networking.client;

public class ClientManager {
    private static ClientManager instance;
    private Client client;

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
}
