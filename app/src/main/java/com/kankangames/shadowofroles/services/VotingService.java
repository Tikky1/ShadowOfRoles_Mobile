package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class VotingService {
    private final BaseGameService gameService;
    private HashMap<Player,Player> votes = new HashMap<>();
    private Player maxVoted;
    private int maxVote;

    public VotingService(BaseGameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Casts a vote from the voter player to the voted player
     * @param voter voter player
     * @param voted voted player
     */
    public void vote(final Player voter, final Player voted){
        votes.put(voter,voted);
    }

    /**
     *
     * @param player the desired player
     * @return player's vote count
     */
    public int getVoteCount(final Player player){
        int count = 0;
        for(Player votedPlayer: votes.values()){
            if(votedPlayer.getNumber()==player.getNumber()){
                count++;
            }
        }
        return count;
    }

    /**
     * Updates the max voted player
     */
    public void updateMaxVoted(){

        final HashMap<Player,Integer> voteCounts = new HashMap<>();

        for(final Player votedPlayer: votes.values()){
            if(votedPlayer!=null) voteCounts.put(votedPlayer, voteCounts.getOrDefault(votedPlayer,0)+1);
        }

        for(final Map.Entry<Player, Integer> entry : voteCounts.entrySet()){
            if(entry.getValue()>maxVote){
                maxVoted = entry.getKey();
                maxVote = entry.getValue();
            }
        }
    }

    /**
     * After the day voting, executes the max voted player if they get more than half of the votes
     */
    public void executeMaxVoted(){
        ArrayList<Player> alivePlayers = gameService.getAlivePlayers();

        for(int i=0;i<alivePlayers.size();i++) {
            Player player = alivePlayers.get(i);
            if(player instanceof AIPlayer){
                AIPlayer aiPlayer = (AIPlayer) player;
                aiPlayer.chooseRandomPlayerVoting(alivePlayers);
                vote(aiPlayer,aiPlayer.getRole().getChoosenPlayer());
            }
        }

        updateMaxVoted();
        if(getMaxVote()>alivePlayers.size()/2){
            for(Player alivePlayer : alivePlayers){
                if(alivePlayer.getNumber() == getMaxVoted().getNumber()){

                    alivePlayer.killPlayer(Time.VOTING, gameService.getTimeService().getDayCount(), CauseOfDeath.HANGING, true);
                    break;
                }
            }


            if(getMaxVoted()!=null){
                gameService.getMessageService().sendMessage(TextManager.getInstance().getText("vote_execute")
                                .replace("{playerName}", getMaxVoted().getName())
                                .replace("{roleName}", getMaxVoted().getRole().getTemplate().getName()),
                        null, true, true);
            }

        }
        gameService.updateAlivePlayers();

        for(Player player: alivePlayers){
            player.getRole().setChoosenPlayer(null);
        }
        clearVotes();
    }

    /**
     * Clears the votes after day is finished
     */
    public void clearVotes(){
        votes.clear();
        maxVoted = null;
        maxVote = 0;
    }

    public void nullifyVotes(){
        votes = null;
        maxVoted = null;
        maxVote = 0;
    }

    // Getters
    public Player getMaxVoted() {
        return maxVoted;
    }

    public int getMaxVote() {
        return maxVote;
    }
}
