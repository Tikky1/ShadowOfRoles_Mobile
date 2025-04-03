package com.kankangames.shadowofroles.game.models.roles.enums;

public enum RoleCategory {
    FOLK_ANALYST(0, Team.FOLK),
    FOLK_PROTECTOR(1, Team.FOLK),
    FOLK_KILLING(2, Team.FOLK),
    FOLK_SUPPORT(3, Team.FOLK),
    FOLK_UNIQUE(4, Team.FOLK),

    CORRUPTER_ANALYST(5, Team.CORRUPTER),
    CORRUPTER_KILLING(6, Team.CORRUPTER),
    CORRUPTER_SUPPORT(7, Team.CORRUPTER),

    NEUTRAL_EVIL(8, Team.NEUTRAL),
    NEUTRAL_CHAOS(9, Team.NEUTRAL),
    NEUTRAL_KILLING(10, Team.NEUTRAL),
    NEUTRAL_GOOD(11, Team.NEUTRAL);

    private final int id;
    private final Team team;


    RoleCategory(int id, Team team){
        this.id = id;
        this.team = team;
    }
    public int getId(){
        return id;
    }

    public Team getTeam() {
        return team;
    }
}
