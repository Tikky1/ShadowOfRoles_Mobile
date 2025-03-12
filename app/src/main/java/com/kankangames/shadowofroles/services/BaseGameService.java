package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.PriorityChangingRole;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.LastJoke;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class BaseGameService {

    protected final ArrayList<Player> allPlayers = new ArrayList<>();
    protected final ArrayList<Player> alivePlayers = new ArrayList<>();

    protected final ArrayList<Player> deadPlayers = new ArrayList<>();
    protected final VotingService votingService;
    protected final TimeService timeService;
    protected final MessageService messageService;
    protected final FinishGameService finishGameService;
    protected final SpecialRolesService specialRolesService;

    public BaseGameService(ArrayList<Player> players){
        timeService = new TimeService();
        votingService = new VotingService(this);
        messageService = new MessageService(this);
        finishGameService = new FinishGameService(this);
        specialRolesService = new SpecialRolesService(this);

        initializePlayers(players);
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

    }

    /**
     *  Performs all abilities according to role priorities
     */
    public void performAllAbilities(){
        ArrayList<Player> players = copyAlivePlayers();

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

    protected ArrayList<Player> copyAlivePlayers(){
        return new ArrayList<>(alivePlayers);
    }


    // Getters
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
