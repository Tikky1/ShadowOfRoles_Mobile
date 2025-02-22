package com.rolegame.game.services;

import com.rolegame.game.gamestate.CauseOfDeath;
import com.rolegame.game.gamestate.Time;
import com.rolegame.game.gamestate.WinnerTeam;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.AIPlayer;
import com.rolegame.game.models.roles.abilities.PriorityChangingRole;
import com.rolegame.game.models.roles.abilities.RoleBlockAbility;
import com.rolegame.game.models.roles.templates.corrupterroles.support.LastJoke;
import com.rolegame.game.models.roles.enums.AbilityType;
import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.models.roles.templates.neutralroles.good.Lorekeeper;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.models.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public final class GameService {
    private final ArrayList<Player> allPlayers = new ArrayList<>();
    private final ArrayList<Player> alivePlayers = new ArrayList<>();

    private final VotingService votingService;
    private final TimeService timeService;
    private final MessageService messageService;
    private final FinishGameService finishGameService;

    private Player currentPlayer;
    private int currentPlayerIndex;
    private int playerCount;



    public GameService(ArrayList<Player> players){
        initializePlayers(players);
        timeService = new TimeService();
        votingService = new VotingService();
        messageService = new MessageService(this);
        finishGameService = new FinishGameService(this);
    }


    /**
     * Initializes the players and distributes their roles
     * @param players players' list
     */
    private void initializePlayers(ArrayList<Player> players){

        playerCount = players.size();

        for(int i=0;i<playerCount;i++){
            System.out.println(players.get(i).getClass().getName());
            allPlayers.add(players.get(i));

        }
        updateAlivePlayers();
        
    }

    public void toggleDayNightCycle(){
        timeService.toggleTimeCycle();
        Time time = timeService.getTime();
        switch (time) {
            case DAY :
                performAllAbilities();
                break;
            case NIGHT:
                executeMaxVoted();
                break;
        }

        if(finishGameService.checkGameFinished()){
            finishGameService.finishGame();
        }
    }

    /**
     *  Performs all abilities according to role priorities
     */
    public void performAllAbilities(){
        ArrayList<Player> players = new ArrayList<>(alivePlayers);

        chooseRandomPlayersForAI(players);

        // If the roles priority changes in each turn changes the priority
        for(Player player: players){
            if (player.getRole().getTemplate() instanceof PriorityChangingRole) {
                PriorityChangingRole priorityChangingRole = (PriorityChangingRole) player.getRole().getTemplate();
                priorityChangingRole.changePriority();
            }
        }

        // Sorts the roles according to priority and if priorities are same sorts
        players.sort(Comparator.comparing((Player player) -> player.getRole().getTemplate().getRolePriority().getPriority()).reversed()
                .thenComparing((Player player) -> player.getRole().getTemplate().getId()));

        // Performs the abilities in the sorted list
        for(Player player: players){
            player.getRole().getTemplate().performAbility(player, player.getRole().getChoosenPlayer(), this);
        }

        // Send some messages to some roles
        for(Player player: players){
            messageService.sendSpecificRoleMessages(player);
        }

        // Resets the players' attributes according to their role
        for(Player player: alivePlayers){
           player.resetStates();
        }
        updateAlivePlayers();
    }



    /**
     * After the day voting, executes the max voted player if they get more than half of the votes
     */
    public void executeMaxVoted(){

        for(int i=0;i<alivePlayers.size();i++) {
            Player player = alivePlayers.get(i);
            if(player instanceof AIPlayer){
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.chooseRandomPlayerVoting(alivePlayers);
                votingService.vote(aiPlayer,aiPlayer.getRole().getChoosenPlayer());
            }
        }

        votingService.updateMaxVoted();
        if(votingService.getMaxVote()>alivePlayers.size()/2){
            for(Player alivePlayer : alivePlayers){
                if(alivePlayer.getNumber() == votingService.getMaxVoted().getNumber()){
                    alivePlayer.setAlive(false);
                    alivePlayer.getCausesOfDeath().add(CauseOfDeath.HANGING);
                    break;
                }
            }


            if(votingService.getMaxVoted()!=null){
                messageService.sendMessage(LanguageManager.getInstance().getText("vote_execute")
                                .replace("{playerName}", votingService.getMaxVoted().getName())
                                .replace("{roleName}", votingService.getMaxVoted().getRole().getTemplate().getName()),
                        null, true, true);
            }

        }
        updateAlivePlayers();

        for(Player player: alivePlayers){
            player.getRole().setChoosenPlayer(null);
        }
        votingService.clearVotes();
    }

    /**
     *If it is morning, he casts a vote for the selected player and sends a message stating who they voted for.
     *If it's night, it sends a message about who is using your role.
     */
    public void sendVoteMessages(){

        final Player chosenPlayer = currentPlayer.getRole().getChoosenPlayer();

        if(timeService.getTime() == Time.VOTING){
            votingService.vote(currentPlayer,chosenPlayer);

            if(chosenPlayer!=null){
                messageService.sendMessage(LanguageManager.getInstance().getText("voted_for")
                                .replace("{playerName}", chosenPlayer.getName())
                        ,currentPlayer,false, true);
            }else{
                messageService.sendMessage(LanguageManager.getInstance().getText("voted_for_none"), currentPlayer, false, true);
            }

        }
        else if(timeService.getTime() == Time.NIGHT){
            AbilityType abilityType = currentPlayer.getRole().getTemplate().getAbilityType();
            if(!(abilityType == AbilityType.PASSIVE || abilityType == AbilityType.NO_ABILITY)){
                if(chosenPlayer!=null){
                    messageService.sendMessage(LanguageManager.getInstance().getText("ability_used_on")
                                    .replace("{playerName}", chosenPlayer.getName())
                            ,currentPlayer,false, false);
                }
                else{
                    messageService.sendMessage(LanguageManager.getInstance().getText("ability_did_not_used"), currentPlayer, false,false);
                }
            }
        }
    }


    /**
     *  Updates alive players
     */
    public void updateAlivePlayers(){
        alivePlayers.clear();
        for (Player player : allPlayers) {

            if (player.isAlive()) {
                alivePlayers.add(player);
            }

            else{

                /* If players role is last joke, player is dead and player has not used ability
                 * yet adds the player to the alive players to use their ability */
                if (player.getRole().getTemplate() instanceof LastJoke) {

                    LastJoke lastJoker = (LastJoke) player.getRole().getTemplate();
                    if (!lastJoker.isDidUsedAbility() && timeService.getTime() == Time.NIGHT) {
                        alivePlayers.add(player);
                    }
                }

            }


        }
        if(!alivePlayers.isEmpty()){
            moveToFirstHumanPlayer();
        }

    }

    /**
     * Updates the dead players
     * @return updated dead players
     */
    public ArrayList<Player> getDeadPlayers() {
        ArrayList<Player> deadPlayers = new ArrayList<>();
        for (Player allPlayer : allPlayers) {
            if (!allPlayer.isAlive()) {
                deadPlayers.add(allPlayer);
            }
        }
        return deadPlayers;
    }



    /**
     * Passes to the turn to the next player
     * @return true if time state is changed
     */
    public boolean passTurn() {

        if(!doesHumanPlayerExist()){
            while(!finishGameService.checkGameFinished()){
                toggleDayNightCycle();
            }
            finishGameService.finishGame();
            return true;
        }

        boolean firstTurn = true;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % alivePlayers.size();
            currentPlayer = alivePlayers.get(currentPlayerIndex);

            if (currentPlayerIndex == findFirstHumanPlayer()) {
                toggleDayNightCycle();
                firstTurn = false;
            }

        } while (currentPlayer instanceof AIPlayer && firstTurn);
        return !firstTurn;
    }

    /**
     * Moves the turn to the first human player, skipping AI players.
     */
    public void moveToFirstHumanPlayer() {

        for(int i=0;i<alivePlayers.size();i++){
            if(!alivePlayers.get(i).isAIPlayer()){
                currentPlayerIndex = i;
                currentPlayer = alivePlayers.get(currentPlayerIndex);
                break;
            }
        }
    }

    public int findFirstHumanPlayer() {

        for(int i=0;i<alivePlayers.size();i++){
            if(!alivePlayers.get(i).isAIPlayer()){
                return i;
            }
        }
        return -1;
    }

    private boolean doesHumanPlayerExist(){
        for (final Player alivePlayer : alivePlayers) {
            if (!alivePlayer.isAIPlayer()) {
                return true;
            }
        }
        return false;

    }

    private void chooseRandomPlayersForAI(List<Player> players){
        for(Player player: players){
            if(player instanceof AIPlayer){
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.chooseRandomPlayerNight(alivePlayers);
            }
        }
    }

    // Getters
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public ArrayList<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public TimeService getTimeService() {
        return timeService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public VotingService getVotingService() {
        return votingService;
    }

    public FinishGameService getFinishGameService() {
        return finishGameService;
    }

}
