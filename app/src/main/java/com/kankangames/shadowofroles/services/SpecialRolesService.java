package com.kankangames.shadowofroles.services;

public final class SpecialRolesService {
    public final int LORE_KEEPER_WINNING_COUNT;

    public SpecialRolesService(BaseGameService gameService) {
        int playerCount = gameService.getAllPlayers().size();
        LORE_KEEPER_WINNING_COUNT = playerCount >=8 ? 3 : 2;
    }

}
