package com.kankangames.shadowofroles.game.models.roles.enums;

public enum Team {
    FOLK(true),
    CORRUPTER(true),
    NEUTRAL(false);

    final boolean isCollaborative;

    Team(boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }

    public boolean isCollaborative() {
        return isCollaborative;
    }
}
