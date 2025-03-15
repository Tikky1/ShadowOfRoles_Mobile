package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.RoleBlockAbility;
import com.kankangames.shadowofroles.models.roles.enums.Team;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;

public final class FinishGameService {

    private transient final BaseGameService gameService;

    private final TreeSet<WinningTeam> winningTeams = new TreeSet<>(Comparator.comparing(WinningTeam::getPriority));

    private boolean isGameFinished = false;
    private Player chillGuyPlayer = null;
    FinishGameService(BaseGameService gameService){
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


            // If one of the players' role can win with other teams finishes the game
            boolean player1CanWinWithOthers = canWinWithOthers(player1.getRole().getTemplate());
            boolean player2CanWinWithOthers = canWinWithOthers(player2.getRole().getTemplate());

            if(player1CanWinWithOthers&&player2CanWinWithOthers){
                return true;
            } else if (player1CanWinWithOthers) {
                winningTeams.add(player2.getRole().getTemplate().getWinningTeam());
                return true;
            }
            else if(player2CanWinWithOthers){
                winningTeams.add(player1.getRole().getTemplate().getWinningTeam());
                return true;
            }

            
            // Finishes the game if the last two players cannot kill each other
            if(player1.getRole().getTemplate().getWinningTeam().getTeam()!=player2.getRole().getTemplate().getWinningTeam().getTeam()
                    &&player2.getRole().getAttack()<=player1.getRole().getDefence()
                    &&player1.getRole().getAttack()<=player2.getRole().getDefence()) {
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

                    winCount = gameService.getSpecialRolesService().LORE_KEEPER_WINNING_COUNT;

                    if (lorekeeper.getTrueGuessCount() >= winCount) {
                        player.setHasWon(true);
                        winningTeams.add(WinningTeam.LORE_KEEPER);
                    }
                    break;

                default:

                    break;
            }

        }


        gameService.messageService.resetMessages();
        gameService.votingService.nullifyVotes();

        if(gameService instanceof MultiDeviceGameService){
            MultiDeviceGameService multiDeviceGameService = (MultiDeviceGameService) gameService;
            multiDeviceGameService.turnTimerService.stopTimer();
        }

    }


    private boolean winnerIsNotOnlyNeutral(){
        return winningTeams.contains(WinningTeam.CORRUPTER) || winningTeams.contains(WinningTeam.FOLK);
    }

    private boolean canWinWithOthers(RoleTemplate roleTemplate){
        if(roleTemplate instanceof NeutralRole){
            NeutralRole neutralRole = (NeutralRole) roleTemplate;

            return neutralRole.canWinWithOtherTeams();
        }
        return false;
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

    public void addWinningTeam(WinningTeam winningTeam){
        winningTeams.add(winningTeam);
    }
}
