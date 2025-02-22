package com.rolegame.game.gamestate;

import com.rolegame.game.models.roles.enums.Team;

public enum WinnerTeam {

    FOLK(-2, Team.FOLK),
    CORRUPTER(-1, Team.CORRUPTER),

    ASSASSIN(1, Team.NEUTRAL),
    CHILL_GUY(2, Team.NEUTRAL),
    CLOWN(3, Team.NEUTRAL),
    LORE_KEEPER(4, Team.NEUTRAL);

    final int priority;

    WinnerTeam(int priority, Team team){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
