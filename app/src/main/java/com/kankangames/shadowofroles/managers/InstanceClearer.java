package com.kankangames.shadowofroles.managers;

import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.services.StartGameService;

public final class InstanceClearer {

    private InstanceClearer (){}

    public static void clearInstances(){
        StartGameService.resetInstance();
        ClientManager.resetManager();
        Server.stopServer();
    }
}
