package com.rolegame.game.models.player;

public class NameAndIsAI {

    private String name;
    private boolean isAI;

    public NameAndIsAI(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
