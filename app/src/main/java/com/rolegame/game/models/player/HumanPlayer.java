package com.rolegame.game.models.player;

import com.rolegame.game.models.roles.Role;

public class HumanPlayer extends Player{
    public HumanPlayer(int number, String name) {
        super(number, name);
    }

    @Override
    public boolean isAIPlayer() {
        return false;
    }
}
