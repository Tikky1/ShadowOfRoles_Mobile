package com.rolegame.game.models.player;

import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.services.RoleService;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.models.roles.templates.folkroles.unique.Entrepreneur;
import com.rolegame.game.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player {
    public AIPlayer(int number, String name) {
        super(number, name);
    }

    @Override
    public boolean isAIPlayer() {
        return true;
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
                if (player.isRevealed()) {
                    if (player.getRole().getTemplate().getWinningTeam().getTeam() == Team.CORRUPTER) {
                        getRole().setChoosenPlayer(player);
                        return;
                    } else if (player.getRole().getTemplate() instanceof NeutralRole) {
                        NeutralRole neutralRole = (NeutralRole) player.getRole().getTemplate();
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
        ArrayList<Player> choosablePlayers = new ArrayList<>(players);
        switch (getRole().getTemplate().getAbilityType()) {
            case NO_ABILITY:
            case PASSIVE:
                return;

            case ACTIVE_SELF:
                getRole().setChoosenPlayer(this);
                return;

            case OTHER_THAN_CORRUPTER:
                for (Player player : players) {
                    if (player.getRole().getTemplate().getWinningTeam().getTeam() == Team.CORRUPTER) {
                        choosablePlayers.remove(player);
                    }
                }
                break;

            case ACTIVE_OTHERS:
                choosablePlayers.remove(this);
                break;

            default:
                break;
        }

        chooseRoleSpecificValues(choosablePlayers);
        if(choosablePlayers.isEmpty()){
            return;
        }
        int randNum = new Random().nextInt(choosablePlayers.size());
        getRole().setChoosenPlayer(choosablePlayers.get(randNum));

    }

    private void chooseRoleSpecificValues(final ArrayList<Player> choosablePlayers) {
        if (getRole().getTemplate() instanceof Entrepreneur) {
            Entrepreneur entrepreneur = (Entrepreneur) getRole().getTemplate();
            boolean randBool = new Random().nextBoolean();
            entrepreneur.setAbilityState(randBool ? Entrepreneur.ChosenAbility.HEAL : Entrepreneur.ChosenAbility.ATTACK);

        } else if (getRole().getTemplate() instanceof Lorekeeper) {
            Lorekeeper lorekeeper = (Lorekeeper) getRole().getTemplate();
            lorekeeper.setGuessedRole(RoleService.getRandomRole());
            choosablePlayers.removeAll(lorekeeper.getAlreadyChosenPlayers());

        }
    }

}

