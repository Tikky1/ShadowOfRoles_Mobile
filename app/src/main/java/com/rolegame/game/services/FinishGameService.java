package com.rolegame.game.services;

import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.models.roles.enums.WinningTeam;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.RoleBlockAbility;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public class FinishGameService {

    private final GameService gameService;

    private final TreeSet<WinningTeam> winningTeams = new TreeSet<>(Comparator.comparing(WinningTeam::getPriority));

    private boolean isGameFinished = false;
    private Player chillGuyPlayer = null;
    FinishGameService(GameService gameService){
        this.gameService = gameService;

    }


    /**
     * Checks the game if it is finished and finishes the game
     * @return game is finished or not
     */
    public boolean checkGameFinished(){

        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();
        // Finishes the game if only 1 player is alive
        if(alivePlayers.size()==1){

            if(alivePlayers.get(0).getRole().getTemplate().isHasNormalWinCondition()){
                winningTeams.add(alivePlayers.get(0).getRole().getTemplate().getWinningTeam());
            }
            return true;
        }

        // Finishes the game if nobody is alive
        if(alivePlayers.isEmpty()){
            return true;
        }


        // If only 2 players are alive checks the game if it is finished
        if(alivePlayers.size()==2){
            Player player1 = gameService.getAlivePlayers().get(0);
            Player player2 = gameService.getAlivePlayers().get(1);

            Optional<Player> player = alivePlayers.stream()
                    .filter(p -> p.getRole().getTemplate() instanceof NeutralRole)
                    .filter(p -> ((NeutralRole) p.getRole().getTemplate()).canWinWithOtherTeams())
                    .findFirst();

            // If one of the players' role is neutral role and the role can win with other teams finishes the game
            if(player.isPresent()){
                winningTeams.add(player1.getRole().getTemplate().getWinningTeam().getTeam() == Team.NEUTRAL ?
                        player2.getRole().getTemplate().getWinningTeam() :
                        player1.getRole().getTemplate().getWinningTeam());
                return true;
            }

            // Finishes the game if the last two players cannot kill each other
            if(player1.getRole().getTemplate().getWinningTeam().getTeam()!=player2.getRole().getTemplate().getWinningTeam().getTeam()
                    &&player2.getAttack()<=player1.getRole().getTemplate().getDefence()
                    &&player1.getAttack()<=player2.getDefence()) {
                return true;
            }

            // Finishes the game if one of the last two players can role block and the other is not immune to role block
            Optional<Player> roleBlockerPlayer = alivePlayers.stream()
                    .filter(p -> p.getRole().getTemplate() instanceof RoleBlockAbility)
                    .findFirst();

            Optional<Player> roleBlockablePlayer = alivePlayers.stream()
                    .filter(p -> !p.getRole().getTemplate().isRoleBlockImmune())
                    .findFirst();

            if(roleBlockerPlayer.isPresent() && roleBlockablePlayer.isPresent() &&
                    player1.getRole().getTemplate().getWinningTeam().getTeam() != player2.getRole().getTemplate().getWinningTeam().getTeam()){
                return true;
            }

        }

        // Continues the game if all players have the same team
        for(int i=0;i<alivePlayers.size()-1;i++){
            if(!alivePlayers.get(i).getRole().getTemplate().getWinningTeam().getTeam().equals(alivePlayers.get(i+1).getRole().getTemplate().getWinningTeam().getTeam())){
                return false;
            }
        }

        // Checks if the living players are neutral if so game continues because they are independent
        if(alivePlayers.get(0).getRole().getTemplate().getWinningTeam().getTeam()!=Team.NEUTRAL){
            for(Player alivePlayer : alivePlayers){
                winningTeams.add(alivePlayer.getRole().getTemplate().getWinningTeam());
            }
            return true;
        }

        return false;

    }

    /**
     * Finishes the game if the end conditions are taken place
     */
    public void finishGame(){
        ArrayList<Player> allPlayers = gameService.getAllPlayers();
        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();

        isGameFinished = true;
        if(!winningTeams.isEmpty() && winnerIsNotOnlyNeutral()){
            for(Player player : allPlayers){

                for(WinningTeam winningTeam : winningTeams){

                    switch (winningTeam){
                        case FOLK:
                        case CORRUPTER:
                            if(player.getRole().getTemplate().getWinningTeam() == winningTeam){
                                player.setHasWon(true);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if(!winnerIsNotOnlyNeutral()){
            alivePlayers.get(0).setHasWon(true);
        }

        for(Player player: allPlayers){

            switch (player.getRole().getTemplate().getId()) {
                case ChillGuy:
                    chillGuyPlayer = player;
                    break;

                case Clown:
                    if (!player.getDeathProperties().isAlive() && !player.getDeathProperties().getCausesOfDeath().contains(CauseOfDeath.HANGING)) {
                        player.setHasWon(true);
                        winningTeams.add(WinningTeam.CLOWN);
                    }
                    break;

                case Lorekeeper:
                    Lorekeeper lorekeeper = (Lorekeeper) player.getRole().getTemplate();
                    int winCount;

                    winCount = gameService.getAllPlayers().size() > 6 ? 3 : 2;

                    if (lorekeeper.getTrueGuessCount() >= winCount) {
                        player.setHasWon(true);
                        winningTeams.add(WinningTeam.LORE_KEEPER);
                    }
                    break;

                default:

                    break;
            }

        }


        gameService.getMessageService().resetMessages();
        gameService.getVotingService().nullifyVotes();

    }


    private boolean winnerIsNotOnlyNeutral(){
        return winningTeams.contains(WinningTeam.CORRUPTER) || winningTeams.contains(WinningTeam.FOLK);
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public WinningTeam getHighestPriorityWinningTeam(){

        if(winningTeams.isEmpty()){
            return null;
        }

        return winningTeams.first();
    }

    public Player getChillGuyPlayer() {
        return chillGuyPlayer;
    }
}
