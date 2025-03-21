package com.kankangames.shadowofroles.ui.activities.multidevice.lobby;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;

import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnKickedFromLobbyListener;

import java.util.List;
import java.util.Locale;

public class GameLobbyActivity extends AbstractLobbyActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plusBtn.setVisibility(GONE);
        minusBtn.setVisibility(GONE);
        startGameBtn.setVisibility(GONE);

        listenerManager.addListener(OnKickedFromLobbyListener.class,
                ()-> showInformationAlert("Do you want to go to the main menu?","You have kicked from lobby") );

    }

    @Override
    protected void updatePlayerList(List<LobbyPlayer> players) {
        if (players != null) {
            playerList.clear();
            playerList.addAll(players);
        }

        runOnUiThread(() -> {
            playerCountText.setText(String.format(Locale.ROOT, "Players: %d", playerList.size()));
            playerAdapter.notifyDataSetChanged();
            Log.d("Lobby", "Adapter notifyDataSetChanged called!");
        });
    }

    @Override
    protected void connectToServer() {
        client.connectToServer(clientManager.getIp());
    }

    @Override
    protected String getAlertTitle() {
        return "Leaving from lobby";
    }

    @Override
    protected String getAlertMessage() {
        return "Do you want to go to the main menu?";
    }

    @Override
    protected void backPressedAction() {
        client.leaveFromLobby();
    }


}