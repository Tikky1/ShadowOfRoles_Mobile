package com.kankangames.shadowofroles.models.roles.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum WinningTeam {

    FOLK(-2, Team.FOLK),
    CORRUPTER(-1, Team.CORRUPTER),

    ASSASSIN(1, Team.NEUTRAL),
    CHILL_GUY(2, Team.NEUTRAL),
    CLOWN(3, Team.NEUTRAL),
    LORE_KEEPER(4, Team.NEUTRAL);

    final int priority;
    final Team team;

    private static final Map<WinningTeam, Set<WinningTeam>> cannotWinWithMap = new HashMap<>();

    static {

        cannotWinWithMap.put(ASSASSIN, EnumSet.of(FOLK, CORRUPTER));
        cannotWinWithMap.put(CORRUPTER, EnumSet.of(ASSASSIN, FOLK));
        cannotWinWithMap.put(FOLK, EnumSet.of(CORRUPTER, ASSASSIN));

    }

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
    public boolean canWinWith(WinningTeam winningTeam){
        return !cannotWinWithMap.getOrDefault(this, EnumSet.noneOf(WinningTeam.class)).contains(winningTeam);
    }

}
