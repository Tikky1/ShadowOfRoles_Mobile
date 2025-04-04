package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.player.AIPlayer;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support.LastJoke;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGameService {

    protected final ArrayList<Player> allPlayers = new ArrayList<>();
    protected final ArrayList<Player> alivePlayers = new ArrayList<>();
    protected final ArrayList<Player> deadPlayers = new ArrayList<>();
    protected final VotingService votingService;
    protected final BaseTimeService timeService;
    protected final MessageService messageService;
    protected final FinishGameService finishGameService;
    protected final AbilityService abilityService;

    public BaseGameService(ArrayList<Player> players, BaseTimeService timeService){
        this.timeService = timeService;
        votingService = new VotingService(this);
        messageService = new MessageService(this);
        finishGameService = new FinishGameService(this);
        abilityService = new AbilityService(this);

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

    public void toggleDayNightCycle() {
        timeService.toggleTimeCycle();
        Time time = timeService.getTime();
        switch (time) {
            case DAY :
                abilityService.performAllAbilities();
                break;
            case NIGHT:
                votingService.executeMaxVoted();
                break;
        }

        finishGameService.finishGame();
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
                if (player.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.HAS_POST_DEATH_EFFECT)) {

                    LastJoke lastJoker = (LastJoke) player.getRole().getTemplate();
                    if (lastJoker.canUseAbility() && timeService.getTime() == Time.NIGHT) {
                        alivePlayers.add(player);
                    }
                }

            }

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

    protected void chooseRandomPlayersForAI(List<Player> players){
        for(Player player: players){
            if(player.isAIPlayer()){
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.chooseRandomPlayerNight(alivePlayers);
            }
        }
    }

    protected boolean doesHumanPlayerExist(){
        for (final Player alivePlayer : alivePlayers) {
            if (!alivePlayer.isAIPlayer()) {
                return true;
            }
        }
        return false;

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

    public BaseTimeService getTimeService() {
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

    public AbilityService getSpecialRolesService() {
        return abilityService;
    }
}
