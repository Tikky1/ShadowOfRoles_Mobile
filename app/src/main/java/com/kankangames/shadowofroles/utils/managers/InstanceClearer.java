package com.kankangames.shadowofroles.utils.managers;

import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.server.Server;
import com.kankangames.shadowofroles.game.services.StartGameService;

public final class InstanceClearer {

    private InstanceClearer (){}

    public static void clearInstances(){
        StartGameService.resetInstance();
        ClientManager.resetManager();
        Server.stopServer();
    }
}
