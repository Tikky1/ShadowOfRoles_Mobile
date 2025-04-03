package com.kankangames.shadowofroles.game.models.gamestate;

public enum GameEndReason {

    SINGLE_PLAYER_REMAINS,
    NO_PLAYERS_ALIVE,
    ONLY_TWO_CANNOT_KILL_EACH_OTHER,
    ONLY_TWO_CAN_WIN_TOGETHER,
    ALL_SAME_TEAM,
    NO_KILLS_IN_MULTIPLE_NIGHTS,
    NONE
}
