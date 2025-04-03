package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.GameConstants;
import com.kankangames.shadowofroles.game.models.gamestate.GameEndReason;
import com.kankangames.shadowofroles.game.models.gamestate.GameEndResult;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.game.models.gamestate.WinStatus;
import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class FinishGameService {

    private transient final BaseGameService gameService;

    private final TreeSet<WinningTeam> winningTeams = new TreeSet<>(Comparator.comparing(WinningTeam::getPriority));
    private final EnumSet<WinningTeam> drawTeams = EnumSet.noneOf(WinningTeam.class);
    private boolean isGameFinished = false;
    private Player chillGuyPlayer = null;
    FinishGameService(BaseGameService gameService){
        this.gameService = gameService;

    }


    /**
     * Checks the game if it is finished and finishes the game
     * @return game is finished or not
     */
    public GameEndResult checkGameFinished(){

        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();

        Set<WinningTeam> currentTeams = alivePlayers.stream().map(player ->
                player.getRole().getTemplate().getWinningTeam()).collect(Collectors.toSet());

        // Finishes the game if only 1 player is alive or nobody is alive
        if(alivePlayers.isEmpty()){
            return new GameEndResult(true, GameEndReason.NO_PLAYERS_ALIVE, WinStatus.TIED);
        }

        if(alivePlayers.size() == 1){
            return new GameEndResult(true, GameEndReason.SINGLE_PLAYER_REMAINS, WinStatus.WON);
        }

        if (currentTeams.size() < 2 && (currentTeams.contains(WinningTeam.FOLK) || currentTeams.contains(WinningTeam.CORRUPTER))) {
            return new GameEndResult(true, GameEndReason.ALL_SAME_TEAM, WinStatus.WON);
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
                    &&player2.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.CAN_KILL_1V1))
                    ||(player1.getRole().getAttack() > player2.getRole().getDefence()
                    &&player1.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.CAN_KILL_1V1));

            // Finishes the game if one of the last two players can role block and the other is not immune to role block
            boolean p1CanBlock = player1.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.CAN_ROLE_BLOCK);
            boolean p1BlockImmune = player1.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE);

            boolean p2CanBlock = player2.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.CAN_ROLE_BLOCK);
            boolean p2BlockImmune = player2.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE);

            boolean roleBlockCheck = (!p1BlockImmune && p2CanBlock) || (!p2BlockImmune && p1CanBlock);


            if(canWinTogether){
                return new GameEndResult(true, GameEndReason.ONLY_TWO_CAN_WIN_TOGETHER, WinStatus.WON);
            }

            if(!playersCanKill || roleBlockCheck){
                return new GameEndResult(true, GameEndReason.ONLY_TWO_CANNOT_KILL_EACH_OTHER, WinStatus.TIED);
            }


        }

        TimePeriod currentPeriod = gameService.timeService.getTimePeriod();
        if(currentPeriod.time() == Time.DAY){
            TimePeriod lastKillPeriod = findLastKilledTime();
            int daysWithoutKilling = currentPeriod.subtract(lastKillPeriod) - 1;
            if(daysWithoutKilling > GameConstants.MAX_NIGHTS_WITHOUT_KILLS)
                return new GameEndResult(true, GameEndReason.NO_KILLS_IN_MULTIPLE_NIGHTS, WinStatus.TIED);
        }

        return new GameEndResult(false, GameEndReason.NONE, WinStatus.UNKNOWN);
    }

    /**
     * Finishes the game if the end conditions are taken place
     */
    public void finishGame(){

        GameEndResult gameEndResult = checkGameFinished();

        if(!gameEndResult.isGameFinished()){
            return;
        }

        isGameFinished = true;

        ArrayList<Player> allPlayers = gameService.getAllPlayers();
        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();

        Player player;
        RoleTemplate role;
        Player player2;
        switch (gameEndResult.getReason()){
            case SINGLE_PLAYER_REMAINS:
                player = alivePlayers.get(0);
                role = player.getRole().getTemplate();
                if(!role.getRoleProperties()
                        .hasAttribute(RoleAttribute.HAS_OTHER_WIN_CONDITION)){
                    winningTeams.add(role.getWinningTeam());
                    player.setWinStatus(WinStatus.WON);
                }
                break;

            case ALL_SAME_TEAM:
                player = alivePlayers.get(0);
                role = player.getRole().getTemplate();
                winningTeams.add(role.getWinningTeam());
                break;

            case ONLY_TWO_CAN_WIN_TOGETHER:
                player = alivePlayers.get(0);
                player2 = alivePlayers.get(1);

                if(player.getRole().getTemplate().getWinningTeam().getPriority()
                        < player2.getRole().getTemplate().getWinningTeam().getPriority()){
                    winningTeams.add(player.getRole().getTemplate().getWinningTeam());
                    player.setWinStatus(WinStatus.WON);
                }
                else{
                    winningTeams.add(player2.getRole().getTemplate().getWinningTeam());
                    player2.setWinStatus(WinStatus.WON);
                }
                break;

            case ONLY_TWO_CANNOT_KILL_EACH_OTHER:
                player = alivePlayers.get(0);
                player2 = alivePlayers.get(1);
                drawTeams.add(player.getRole().getTemplate().getWinningTeam());
                drawTeams.add(player2.getRole().getTemplate().getWinningTeam());
                player.setWinStatus(WinStatus.TIED);
                player2.setWinStatus(WinStatus.TIED);
                break;

            case NO_PLAYERS_ALIVE:
                ArrayList<Player> lastLivingPlayers = findLastLivingPlayers();
                for(Player player1 : lastLivingPlayers){
                    role = player1.getRole().getTemplate();
                    if(!player1.getRole().getTemplate().getRoleProperties()
                            .hasAttribute(RoleAttribute.HAS_OTHER_WIN_CONDITION)){
                        drawTeams.add(role.getWinningTeam());
                        player1.setWinStatus(WinStatus.TIED);
                    }
                }
                break;

            case NO_KILLS_IN_MULTIPLE_NIGHTS:
                for(Player player1 : alivePlayers){
                    if(!player1.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.HAS_OTHER_WIN_CONDITION)){
                        drawTeams.add(player1.getRole().getTemplate().getWinningTeam());
                        player1.setWinStatus(WinStatus.TIED);
                    }
                }
                break;

        }

        for(Player player1 : allPlayers){
            if(!player1.getRole().getTemplate().getRoleProperties()
                    .hasAttribute(RoleAttribute.WINS_ALONE)){

                if(winningTeams.contains(player1.getRole().getTemplate().getWinningTeam())){
                    player1.setWinStatus(WinStatus.WON);
                } else if (drawTeams.contains(player1.getRole().getTemplate().getWinningTeam())) {
                    player1.setWinStatus(WinStatus.TIED);
                }
            }

        }


        for(Player player3: allPlayers){

            switch (player3.getRole().getTemplate().getWinningTeam()) {
                case CHILL_GUY:
                    chillGuyPlayer = player3;
                    break;

                case CLOWN:
                    if (!player3.getDeathProperties().isAlive() && !player3.getDeathProperties().getCausesOfDeath().contains(CauseOfDeath.HANGING)) {
                        player3.setWinStatus(WinStatus.WON);
                        addWinningTeam(WinningTeam.CLOWN);
                    }
                    break;

                case LORE_KEEPER:
                    Lorekeeper lorekeeper = (Lorekeeper) player3.getRole().getTemplate();
                    int winCount  = gameService.getSpecialRolesService().LORE_KEEPER_WINNING_COUNT;

                    if (lorekeeper.getTrueGuessCount() >= winCount) {
                        player3.setWinStatus(WinStatus.WON);
                        addWinningTeam(WinningTeam.LORE_KEEPER);
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

    private ArrayList<Player> findLastLivingPlayers(){
        if(!gameService.alivePlayers.isEmpty()){
            return gameService.alivePlayers;
        }
        TimePeriod timePeriod = findLastKilledTime();
        ArrayList<Player> players = new ArrayList<>();

        for(Player player : gameService.allPlayers){
            if(player.getDeathProperties().getDeathTimePeriod().equals(timePeriod)){
                players.add(player);
            }
        }
        return players;
    }

    private TimePeriod findLastKilledTime(){
        TimePeriod timePeriod = new TimePeriod(Time.DAY, -1);

        for(Player player : gameService.allPlayers){
            if(player.getDeathProperties().getDeathTimePeriod().isAfter(timePeriod)){
                timePeriod = player.getDeathProperties().getDeathTimePeriod();
            }
        }
        return timePeriod;
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
