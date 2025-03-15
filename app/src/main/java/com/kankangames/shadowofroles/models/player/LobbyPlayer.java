package com.kankangames.shadowofroles.models.player;

import com.kankangames.shadowofroles.models.player.properties.LobbyPlayerStatus;

public class LobbyPlayer {

    private final String name;
    private final boolean isHost;
    private final boolean isAI;
    private LobbyPlayerStatus lobbyPlayerStatus;

    public LobbyPlayer(String name, boolean isHost, boolean isAI, LobbyPlayerStatus lobbyPlayerStatus) {
        this.name = name;
        this.isHost = isHost;
        this.isAI = isAI;
        this.lobbyPlayerStatus = lobbyPlayerStatus;
    }

    public String getName() {
        return name;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isAI() {
        return isAI;
    }

    public LobbyPlayerStatus getLobbyPlayerStatus() {
        return lobbyPlayerStatus;
    }

    public void setLobbyPlayerStatus(LobbyPlayerStatus lobbyPlayerStatus) {
        this.lobbyPlayerStatus = lobbyPlayerStatus;
    }
}
