package com.kankangames.shadowofroles.models.player;

import com.kankangames.shadowofroles.models.roles.abilities.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.services.RoleService;
import com.kankangames.shadowofroles.models.roles.enums.Team;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;

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
        if(getRole().getTemplate().getWinningTeam().getTeam() == Team.CORRUPTER){
            for(Player player : players){
                if(player.getRole().getTemplate().getWinningTeam().getTeam() == Team.CORRUPTER){
                    choosablePlayers.remove(player);
                }
            }
        }
        else if (getRole().getTemplate().getWinningTeam().getTeam() == Team.FOLK) {
            for (Player player : players) {
                if (player.getRole().isRevealed()) {
                    if (player.getRole().getTemplate().getWinningTeam().getTeam() == Team.CORRUPTER) {
                        role.setChoosenPlayer(player);
                        return;
                    } else if (player.role.getTemplate() instanceof NeutralRole) {
                        NeutralRole neutralRole = (NeutralRole) player.role.getTemplate();
                        if (!neutralRole.canWinWithOtherTeams()) {
                            getRole().setChoosenPlayer(player);
                            return;
                        }
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
            RoleSpecificValuesChooser roleSpecificValuesChooser = (RoleSpecificValuesChooser) role;
            roleSpecificValuesChooser.chooseRoleSpecificValues(choosablePlayers);
        }
    }

}

