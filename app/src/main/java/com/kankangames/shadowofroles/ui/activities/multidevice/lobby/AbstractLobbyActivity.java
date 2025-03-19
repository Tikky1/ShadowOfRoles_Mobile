package com.kankangames.shadowofroles.ui.activities.multidevice.lobby;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDisbandedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ConnectionListener;
import com.kankangames.shadowofroles.services.DataProvider;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.activities.ImageChangingActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.activities.game.MultipleDeviceGameActivity;
import com.kankangames.shadowofroles.ui.activities.multidevice.OnlineSelectionActivity;
import com.kankangames.shadowofroles.ui.adapters.LobbyPlayersAdapter;
import com.kankangames.shadowofroles.ui.alerts.AlertProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLobbyActivity extends ImageChangingActivity {

    protected List<LobbyPlayer> playerList;
    protected LobbyPlayersAdapter playerAdapter;
    protected Client client;
    protected NetworkListenerManager listenerManager;

    protected ImageView backgroundImage;
    protected Button plusBtn, minusBtn, closeBtn, startGameBtn;
    protected RecyclerView playersView;
    protected ClientManager clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multidevice_host_lobby);

        playersView = findViewById(R.id.hosting_lobby_recycler_view);
        startGameBtn = findViewById(R.id.hosting_start_game_button);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        closeBtn = findViewById(R.id.close_button);
        backgroundImage = findViewById(R.id.backgroundImage);
        changeImage();

        clientManager = ClientManager.getInstance();
        client = clientManager.getClient();
        listenerManager = client.getClientListenerManager();


        connectToServer();

        if(client.getLobbyPlayers() != null){
            playerList = client.getLobbyPlayers();
        }
        else{
            playerList = new ArrayList<>();
        }

        playerAdapter = new LobbyPlayersAdapter(playerList, this);
        playersView.setAdapter(playerAdapter);
        playersView.setLayoutManager(new LinearLayoutManager(this));

        listenerManager.addListener(ConnectionListener.class,
                new ConnectionListener() {
                    @Override
                    public void onConnectionSuccessful(List<LobbyPlayer> players) {
                        updatePlayerList(players);
                    }

                    @Override
                    public void onConnectionFailed(String errorMessage) {
                        runOnUiThread(()->{
                            AlertProvider.showGeneralAlert(AbstractLobbyActivity.this,
                                    "Error", errorMessage, () ->{
                                Intent intent = new Intent(AbstractLobbyActivity.this
                                        ,OnlineSelectionActivity.class);
                                startActivity(intent);
                                backPressedAction();
                                    }
                                    );
                        });

                    }
                });
        listenerManager.addListener(OnGameStartingListener.class, this::startGame);
        listenerManager.addListener(OnGameDisbandedListener.class,
                ()-> showInformationAlert("Do you want to go to the main menu?", "Game is disbanded"));
        closeBtn.setOnClickListener(v ->
                showConfirmationAlert(getAlertMessage(), getAlertTitle(), this::backPressedAction) );
        handleBackPressed();
    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }

    protected void showInformationAlert(String message, String title){

        runOnUiThread(()-> {
            if(isFinishing()){
                return;
            }
            new AlertDialog.Builder(this)

                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("YES", (dialog, which) -> {

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setCancelable(false)
                    .show();
        });

    }

    protected void showConfirmationAlert(String message, String title, Runnable action){

        runOnUiThread(()-> {
            if(isFinishing()){
                return;
            }
            AlertDialog.Builder builder =  new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("YES", (dialog, which) -> {
                        new Thread(action).start();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("NO",  (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });

    }


    private void handleBackPressed(){
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new Thread(() -> {


                    runOnUiThread(() -> {

                        showConfirmationAlert(getAlertMessage(),getAlertTitle(),
                                ()-> {

                                    backPressedAction();
                                    commonBackPressedAction();
                                });
                    });
                }).start();

            }
        });
    }



    private void commonBackPressedAction(){
        Intent intent = new Intent(AbstractLobbyActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void startGame(DataProvider gameData){
        StartGameService startGameService = StartGameService.getInstance();
        startGameService.setGameMode(GameMode.MULTIPLE_DEVICE);
        startGameService.setGameService(gameData);
        Intent intent = new Intent(this, MultipleDeviceGameActivity.class);
        startActivity(intent);
    }

    protected abstract void updatePlayerList(List<LobbyPlayer> players);
    protected abstract void connectToServer();

    protected abstract String getAlertTitle();

    protected abstract String getAlertMessage();

    protected abstract void backPressedAction();
}
