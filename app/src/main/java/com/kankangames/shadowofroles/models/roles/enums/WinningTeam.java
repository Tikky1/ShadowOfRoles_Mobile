package com.kankangames.shadowofroles.models.roles.enums;

public enum WinningTeam {

    FOLK(-2, Team.FOLK),
    CORRUPTER(-1, Team.CORRUPTER),

    ASSASSIN(1, Team.NEUTRAL),
    CHILL_GUY(2, Team.NEUTRAL),
    CLOWN(3, Team.NEUTRAL),
    LORE_KEEPER(4, Team.NEUTRAL);

    final int priority;
    final Team team;

    WinningTeam(int priority, Team team){
        this.priority = priority;
        this.team = team;
    }

    public int getPriority() {
        return priority;
    }

    public Team getTeam() {
        return team;
    }
}
