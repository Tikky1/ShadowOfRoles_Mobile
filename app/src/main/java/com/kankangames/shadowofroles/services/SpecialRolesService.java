package com.kankangames.shadowofroles.services;

public class SpecialRolesService {
    public final int LORE_KEEPER_WINNING_COUNT;

    public SpecialRolesService(GameService gameService) {
        int playerCount = gameService.getAllPlayers().size();
        LORE_KEEPER_WINNING_COUNT = playerCount >=8 ? 3 : 2;
    }

}
