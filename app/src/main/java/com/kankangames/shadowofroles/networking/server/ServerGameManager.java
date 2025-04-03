package com.kankangames.shadowofroles.networking.server;

import com.google.gson.Gson;
import com.kankangames.shadowofroles.game.models.player.AIPlayer;
import com.kankangames.shadowofroles.game.models.player.HumanPlayer;
import com.kankangames.shadowofroles.game.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.properties.Role;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.EndGameDTO;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.GameDTO;
import com.kankangames.shadowofroles.networking.jsonutils.GsonProvider;
import com.kankangames.shadowofroles.game.services.MultiDeviceGameService;
import com.kankangames.shadowofroles.game.services.RoleService;
import com.kankangames.shadowofroles.game.services.TurnTimerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ServerGameManager implements TurnTimerService.OnTimeChangeListener{

    MultiDeviceGameService multiDeviceGameService;
    boolean isGameStarted = false;
    private final List<LobbyPlayer> lobbyPlayers;
    private final Server server;
    private boolean chillGuyHandled = false;

    ServerGameManager(Server server){

        this.server = server;
        this.lobbyPlayers = server.getServerLobbyManager().getLobbyPlayers();
    }

//    void playerLeft(Player player, boolean isHost){
//        if(isHost){
//
//        }
//        else {
//            AIPlayer aiPlayer = Player.makeAI(player);
//            multiDeviceGameService.p
//        }
//    }

    public void startGame() {
        ArrayList<Player> players = new ArrayList<>();
        int aiPlayerNum = 1;
        int humanPlayerCount = 0;
        for(int i=0; i < lobbyPlayers.size() ;i++){
            LobbyPlayer lobbyPlayer = lobbyPlayers.get(i);
            if(lobbyPlayer.isAI()){
                players.add(new AIPlayer(i+1, "Bot " + aiPlayerNum));
                aiPlayerNum++;
            }
            else{
                players.add(new HumanPlayer(i+1, server.getClients().get(humanPlayerCount).getClientPlayer().getName()));
                humanPlayerCount++;
            }
        }

        isGameStarted = true;
        multiDeviceGameService = new MultiDeviceGameService(players, this);

        ArrayList<RoleTemplate> roleTemplates = RoleService.initializeRoles(players.size());
        for(int i=0;i<players.size();i++){
            players.get(i).setRole(new Role(roleTemplates.get(i)));
        }
        new Thread( ()-> sendGameData(false)).start();

    }



    void sendGameData(boolean didGameStarted){

        Gson gson = GsonProvider.getGson();
        boolean isGameEnded = multiDeviceGameService.getFinishGameService().isGameFinished();

        if(isGameEnded){

            EndGameDTO endGameDTO = new EndGameDTO(
                    multiDeviceGameService.getFinishGameService(),
                    multiDeviceGameService.getAllPlayers()
            );
            Optional<Player> chillGuyPlayer = Optional.ofNullable(multiDeviceGameService.getFinishGameService().getChillGuyPlayer());

            String jsonEndGameData = gson.toJson(endGameDTO, EndGameDTO.class);
            if(chillGuyPlayer.isPresent() && !chillGuyHandled){
                server.broadcastMessage("WAITING_CHILLGUY:" + jsonEndGameData);
                chillGuyHandled = true;
            }
            else{
                server.broadcastMessage("GAME_ENDED:" + jsonEndGameData);
            }


        }

        else{
            int humanPlayerCount = 0;
            for (int i=0; i < lobbyPlayers.size(); i++){
                if(lobbyPlayers.get(i).isAI()){
                    continue;
                }
                Player player = multiDeviceGameService.findPlayer(i+1);
                GameDTO gameDTO = new GameDTO(
                        multiDeviceGameService.getMessageService().getPlayerMessages(player),
                        multiDeviceGameService.getDeadPlayers(),
                        multiDeviceGameService.getAlivePlayers(),
                        multiDeviceGameService.getTimeService(),
                        multiDeviceGameService.getFinishGameService().isGameFinished(),
                        i+1
                );


                String jsonGameData = gson.toJson(gameDTO);
                String message = (didGameStarted ? "GAME_DATA:" : "GAME_STARTED:" ) + jsonGameData;
                server.getClients().get(humanPlayerCount).sendMessage(message);
                humanPlayerCount++;
            }
        }


    }

    @Override
    public void onTimeChange() {
        sendGameData(true);
    }
}
