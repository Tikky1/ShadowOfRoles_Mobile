package com.rolegame.game.models.roles.enums;

public enum RolePriority {

    NONE(0),
    HEAL(1),
    IMMUNE(2),
    BLINDER(3),
    ROLE_BLOCK(4),
    LORE_KEEPER(5),
    LAST_JOKE(6);




    private final int priority;

    RolePriority(int priority){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
