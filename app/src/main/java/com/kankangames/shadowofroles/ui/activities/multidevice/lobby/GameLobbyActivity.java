package com.kankangames.shadowofroles.ui.activities.multidevice.lobby;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;

import com.kankangames.shadowofroles.R;
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
                ()-> showInformationAlert(getString(R.string.main_menu_alert),
                        getString(R.string.kicked_from_lobby)));

    }

    @Override
    protected void updatePlayerList(List<LobbyPlayer> players) {
        if (players != null) {
            playerList.clear();
            playerList.addAll(players);
        }

        runOnUiThread(() -> {
            playerCountText.setText(String.format(Locale.ROOT, getText(R.string.players_count).toString(), playerList.size()));
            playerAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void connectToServer() {
        client.connectToServer(clientManager.getIp());
    }

    @Override
    protected String getAlertTitle() {
        return getString(R.string.leaving_from_lobby);
    }

    @Override
    protected String getAlertMessage() {
        return getString(R.string.main_menu_alert);
    }

    @Override
    protected void backPressedAction() {
        client.leaveFromLobby();
    }


}