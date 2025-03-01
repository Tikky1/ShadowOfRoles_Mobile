package com.kankangames.shadowofroles.models.player;

public class HumanPlayer extends Player{
    public HumanPlayer(int number, String name) {
        super(number, name);
    }

    @Override
    public boolean isAIPlayer() {
        return false;
    }
}
