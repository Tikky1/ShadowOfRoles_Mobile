package com.rolegame.game.models.roles.enums;

import com.rolegame.game.gamestate.WinnerTeam;

public enum RoleID {

    Detective(1,WinnerTeam.FOLK),
    Observer(2,WinnerTeam.FOLK),
    Soulbinder(3,WinnerTeam.FOLK),
    Stalker(4,WinnerTeam.FOLK),
    Psycho(5, WinnerTeam.CORRUPTER),
    DarkRevealer(6, WinnerTeam.CORRUPTER),
    Interrupter(7, WinnerTeam.CORRUPTER),
    SealMaster(8,WinnerTeam.FOLK),
    Assassin(9, WinnerTeam.ASSASSIN),
    ChillGuy(10, WinnerTeam.CHILL_GUY),
    LastJoke(11, WinnerTeam.CORRUPTER),
    Clown(12, WinnerTeam.CLOWN),
    Disguiser(13, WinnerTeam.CORRUPTER),
    Darkseer(14, WinnerTeam.CORRUPTER),
    Blinder(15, WinnerTeam.CORRUPTER),
    FolkHero(16,WinnerTeam.FOLK),
    Entrepreneur(17,WinnerTeam.FOLK),
    Lorekeeper(18,WinnerTeam.LORE_KEEPER);

    final WinnerTeam winnerTeam;
    RoleID(int i, WinnerTeam winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public WinnerTeam getWinnerTeam() {
        return winnerTeam;
    }
}
