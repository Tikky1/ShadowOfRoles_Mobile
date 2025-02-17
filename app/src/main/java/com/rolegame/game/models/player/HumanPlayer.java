package com.rolegame.game.models.player;

import com.rolegame.game.models.roles.Role;

public class HumanPlayer extends Player{
    public HumanPlayer(int number, String name, Role role) {
        super(number, name, role);
    }

    @Override
    public boolean isAIPlayer() {
        return false;
    }
}
