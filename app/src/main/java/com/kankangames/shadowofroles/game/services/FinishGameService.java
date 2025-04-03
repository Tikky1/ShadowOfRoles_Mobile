package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.WinStatus;
import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

        Set<WinningTeam> currentTeams = alivePlayers.stream().map(player ->
                player.getRole().getTemplate().getWinningTeam()).collect(Collectors.toSet());

        // Finishes the game if only 1 player is alive or nobody is alive
        if(alivePlayers.size() == 1 || alivePlayers.isEmpty()){
            return true;
        }


        // If only 2 players are alive checks the game if it is finished
        if(alivePlayers.size() == 2){

            Player player1 = gameService.getAlivePlayers().get(0);
            Player player2 = gameService.getAlivePlayers().get(1);


            // If one of the players' role can win with other teams finishes the game
            boolean canWinTogether = player1.getRole().getTemplate().getWinningTeam()
                    .canWinWith(player2.getRole().getTemplate().getWinningTeam());


            // Finishes the game if the last two players cannot kill each other
            boolean playersCanKill = (player2.getRole().getAttack() > player1.getRole().getDefence()
                    &&player2.getRole().getTemplate().getRoleProperties().canWin1v1())
                    ||(player1.getRole().getAttack() > player2.getRole().getDefence()
                    &&player1.getRole().getTemplate().getRoleProperties().canWin1v1());

            // Finishes the game if one of the last two players can role block and the other is not immune to role block
            boolean p1CanBlock = player1.getRole().getTemplate().getRoleProperties().canRoleBlock();
            boolean p1BlockImmune = player1.getRole().getTemplate().getRoleProperties().isRoleBlockImmune();

            boolean p2CanBlock = player2.getRole().getTemplate().getRoleProperties().canRoleBlock();
            boolean p2BlockImmune = player2.getRole().getTemplate().getRoleProperties().isRoleBlockImmune();

            boolean roleBlockCheck = (!p1BlockImmune && p2CanBlock) || (!p2BlockImmune && p1CanBlock);


            if(canWinTogether || !playersCanKill || roleBlockCheck ){
                return true;
            }


        }

        // Finishes the game if all players have the same team
        // Checks if the living players are neutral if so game continues because they are independent
        return currentTeams.size() < 2 && (currentTeams.contains(WinningTeam.FOLK) || currentTeams.contains(WinningTeam.CORRUPTER));
    }

    /**
     * Finishes the game if the end conditions are taken place
     */
    public void finishGame(){

        isGameFinished = true;

        ArrayList<Player> allPlayers = gameService.getAllPlayers();
        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();

        // Checks if the game is draw
        boolean isDraw = false;
        Set<WinningTeam> drawTeams = new HashSet<>();

        for (Player player : alivePlayers) {
            WinningTeam playerTeam = player.getRole().getTemplate().getWinningTeam();

            for (Player otherPlayer : alivePlayers) {
                if (player == otherPlayer) continue;

                WinningTeam otherTeam = otherPlayer.getRole().getTemplate().getWinningTeam();

                if (!playerTeam.canWinWith(otherTeam)) {
                    isDraw = true;
                    drawTeams.add(playerTeam);
                    drawTeams.add(otherTeam);
                    player.setWinStatus(WinStatus.TIED);
                    otherPlayer.setWinStatus(WinStatus.TIED);
                    break;
                }
            }

            if (isDraw) break;
        }


        if(isDraw){
            for (Player player : allPlayers) {
                if (drawTeams.contains(player.getRole().getTemplate().getWinningTeam())
                && !player.getRole().getTemplate().getRoleProperties().winsAlone()) {
                    player.setWinStatus(WinStatus.TIED);
                }
            }
        }
        else{

            for (Player player : alivePlayers) {
                WinningTeam team = player.getRole().getTemplate().getWinningTeam();
                if (player.getRole().getTemplate().getRoleProperties().hasNormalWinCondition()) {
                    winningTeams.add(team);
                    player.setWinStatus(WinStatus.WON);
                }
            }

            for (Player player : allPlayers) {
                WinningTeam playerTeam = player.getRole().getTemplate().getWinningTeam();

                if (winningTeams.contains(playerTeam) && !player.getRole().getTemplate().getRoleProperties().winsAlone()) {
                    player.setWinStatus(WinStatus.WON);
                }
            }
        }

        // If folk or corrupter wins, makes all team members won
        for(WinningTeam winningTeam : winningTeams){

            for(Player player : allPlayers){

                switch (winningTeam){
                    case FOLK:
                    case CORRUPTER:
                        if(player.getRole().getTemplate().getWinningTeam() == winningTeam){
                            player.setWinStatus(WinStatus.WON);
                            continue;
                        }
                        break;
                    default:
                        break;
                }
            }
        }


        for(Player player: allPlayers){

            switch (player.getRole().getTemplate().getWinningTeam()) {
                case CHILL_GUY:
                    chillGuyPlayer = player;
                    break;

                case CLOWN:
                    if (!player.getDeathProperties().isAlive() && !player.getDeathProperties().getCausesOfDeath().contains(CauseOfDeath.HANGING)) {
                        player.setWinStatus(WinStatus.WON);
                        winningTeams.add(WinningTeam.CLOWN);
                    }
                    break;

                case LORE_KEEPER:
                    Lorekeeper lorekeeper = (Lorekeeper) player.getRole().getTemplate();
                    int winCount;

                    winCount = gameService.getSpecialRolesService().LORE_KEEPER_WINNING_COUNT;

                    if (lorekeeper.getTrueGuessCount() >= winCount) {
                        player.setWinStatus(WinStatus.WON);
                        winningTeams.add(WinningTeam.LORE_KEEPER);
                    }
                    break;

                default:
                    break;
            }

        }

        resetGame();
    }

    private void resetGame(){
        gameService.messageService.resetMessages();
        gameService.votingService.nullifyVotes();

        if(gameService instanceof MultiDeviceGameService){
            MultiDeviceGameService multiDeviceGameService = (MultiDeviceGameService) gameService;
            multiDeviceGameService.turnTimerService.stopTimer();
        }
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
