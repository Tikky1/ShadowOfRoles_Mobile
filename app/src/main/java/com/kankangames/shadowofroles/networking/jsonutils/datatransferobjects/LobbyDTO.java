package com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects;


import com.kankangames.shadowofroles.game.models.player.LobbyPlayer;

import java.util.List;
import java.util.Optional;

public class LobbyData {
    private final List<LobbyPlayer> players;
    private final int id;

    public LobbyData(List<LobbyPlayer> players, int id) {
        this.players = players;
        this.id = id;
    }

    public List<LobbyPlayer> getPlayers() {
        return players;
    }

    public int getId() {
        return id;
    }

    public Optional<LobbyPlayer> getPlayer(){
        return players.stream().filter(lobbyPlayer -> lobbyPlayer.getId() == id).findAny();
    }
}
