package com.kankangames.shadowofroles.game.models.player;

import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.enums.Team;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {
    public AIPlayer(int number, String name) {
        super(number, name, true);
    }

    public void chooseRandomPlayerVoting(final ArrayList<Player> players){
        ArrayList<Player> choosablePlayers = new ArrayList<>(players);
        choosablePlayers.remove(this);
        WinningTeam team = role.getTemplate().getWinningTeam();
        if(role.getTemplate().getRoleProperties().hasAttribute(RoleAttribute.KNOWS_TEAM_MEMBERS)){
            for(Player player : players){
                if(player.getRole().getTemplate().getWinningTeam() == team){
                    choosablePlayers.remove(player);
                }
            }
        }
        else if (getRole().getTemplate().getWinningTeam().getTeam() == Team.FOLK) {
            for (Player player : players) {
                if (player.getRole().isRevealed()) {
                    if (!player.getRole().getTemplate().getWinningTeam().canWinWith(WinningTeam.FOLK)) {
                        role.setChoosenPlayer(player);
                        return;
                    } else if (player.getRole().getTemplate().getWinningTeam().getTeam() == Team.FOLK) {
                        choosablePlayers.remove(player);
                    }
                }
            }
        }

        if(choosablePlayers.isEmpty()){
            return;
        }
        int randNum = new Random().nextInt(choosablePlayers.size());
        getRole().setChoosenPlayer(choosablePlayers.get(randNum));
    }

    public void chooseRandomPlayerNight(final ArrayList<Player> players){
        List<Player> choosablePlayers = new ArrayList<>(players);
        choosablePlayers = role.getTemplate().filterChoosablePlayers(this, choosablePlayers);

        chooseRoleSpecificValues(choosablePlayers);
        if(choosablePlayers.isEmpty()){
            return;
        }
        int randNum = new Random().nextInt(choosablePlayers.size());
        getRole().setChoosenPlayer(choosablePlayers.get(randNum));

    }

    private void chooseRoleSpecificValues(final List<Player> choosablePlayers) {
        if(role.getTemplate() instanceof RoleSpecificValuesChooser){
            RoleSpecificValuesChooser roleSpecificValuesChooser = (RoleSpecificValuesChooser) role.getTemplate();
            roleSpecificValuesChooser.chooseRoleSpecificValues(choosablePlayers);
        }
    }

}

