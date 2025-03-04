package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.LanguageManager;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.roles.abilities.PriorityChangingRole;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.LastJoke;
import com.kankangames.shadowofroles.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.*;

public final class GameService {
    private final ArrayList<Player> allPlayers = new ArrayList<>();
    private final ArrayList<Player> alivePlayers = new ArrayList<>();

    private final ArrayList<Player> deadPlayers = new ArrayList<>();
    private final VotingService votingService;
    private final TimeService timeService;
    private final MessageService messageService;
    private final FinishGameService finishGameService;
    private final SpecialRolesService specialRolesService;

    private Player currentPlayer;
    private int currentPlayerIndex;


    public GameService(ArrayList<Player> players){
        initializePlayers(players);
        timeService = new TimeService();
        votingService = new VotingService(this);
        messageService = new MessageService(this);
        finishGameService = new FinishGameService(this);
        specialRolesService = new SpecialRolesService(this);

    }


    /**
     * Initializes the players and distributes their roles
     * @param players players' list
     */
    private void initializePlayers(ArrayList<Player> players){

        allPlayers.addAll(players);
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
                votingService.executeMaxVoted();
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
           player.getRole().resetStates();
        }
        updateAlivePlayers();
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
                                .replace("{playerName}", chosenPlayer.getNameAndNumber())
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
                                    .replace("{playerName}", chosenPlayer.getNameAndNumber())
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

            if (player.getDeathProperties().isAlive()) {
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
        for (Player player : allPlayers) {
            if (!player.getDeathProperties().isAlive() && !deadPlayers.contains(player)) {
                deadPlayers.add(player);
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
    private void moveToFirstHumanPlayer() {

        for(int i=0;i<alivePlayers.size();i++){
            if(!alivePlayers.get(i).isAIPlayer()){
                currentPlayerIndex = i;
                currentPlayer = alivePlayers.get(currentPlayerIndex);
                break;
            }
        }
    }

    private int findFirstHumanPlayer() {

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

    public SpecialRolesService getSpecialRolesService() {
        return specialRolesService;
    }
}
