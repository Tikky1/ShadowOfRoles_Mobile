package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.content.res.AppCompatResources;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.InstanceClearer;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.jsonobjects.EndGameData;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.ChillGuyListener;
import com.kankangames.shadowofroles.services.DataProvider;
import com.kankangames.shadowofroles.services.FinishGameService;
import com.kankangames.shadowofroles.services.SingleDeviceGameService;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.fragments.BlackScreenFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.ChillGuyFragment;

import java.util.ArrayList;
import java.util.Locale;

public class GameEndActivity extends BaseActivity{

    private TableLayout endGameTable;

    private Button mainMenuBtn;

    private TextView winnerTeamText;
    private ImageView winnerTeamImage;

    private FinishGameService finishGameService;
    private ArrayList<Player> allPlayers;

    private WinningTeam winningTeam;
    private GameMode gameMode;
    private Client client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_end);
        StartGameService startGameService = StartGameService.getInstance();


        endGameTable = findViewById(R.id.end_game_table);
        mainMenuBtn = findViewById(R.id.go_back_main_button);
        winnerTeamText = findViewById(R.id.winner_team_text);
        winnerTeamImage = findViewById(R.id.winner_team_image);

        gameMode = startGameService.getGameMode();


        if(gameMode == GameMode.SINGLE_DEVICE){
            DataProvider dataProvider = StartGameService.getInstance().getGameService();
            SingleDeviceGameService singleDeviceGameService = (SingleDeviceGameService) dataProvider;
            finishGameService = singleDeviceGameService.getFinishGameService();
            allPlayers = singleDeviceGameService.getAllPlayers();

            boolean chillGuyExist = createChillGuyAlert();
            if(!chillGuyExist){
                setActivity();
            }

        }
        else{

            client = ClientManager.getInstance().getClient();
            EndGameData endGameData = client.getClientGameManager().getEndGameData();
            finishGameService = endGameData.getFinishGameService();
            allPlayers = endGameData.getAllPlayers();

            if(endGameData.getFinishGameService().getChillGuyPlayer()!= null){
                createBlankAlert();
                if(client.getClientLobbyManager().isHost()){
                    createChillGuyAlert();
                }
            }else{
                setActivity();
            }


        }


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goMainMenu();
            }
        });


    }

    private void createBlankAlert() {
        BlackScreenFragment blackScreenFragment = new BlackScreenFragment();
        blackScreenFragment.show(getSupportFragmentManager(), "Black Screen");
        client.getClientListenerManager().addListener(ChillGuyListener.class,
                () -> {
                    blackScreenFragment.dismiss();
                    EndGameData endGameData = client.getClientGameManager().getEndGameData();
                    finishGameService = endGameData.getFinishGameService();
                    allPlayers = endGameData.getAllPlayers();
                    setActivity();
                });
    }

    private boolean createChillGuyAlert(){
        Player chillGuyPlayer = finishGameService.getChillGuyPlayer();

        if(chillGuyPlayer == null){
            return false;
        }

        ChillGuyFragment chillGuyFragment = new ChillGuyFragment(
                gameMode == GameMode.SINGLE_DEVICE ? this::setActivity : ()->{}
                , chillGuyPlayer, finishGameService, gameMode);
        chillGuyFragment.show(getSupportFragmentManager(), "Chill Guy Alert");

        return true;

    }

    private void setActivity(){
        winningTeam = finishGameService.getHighestPriorityWinningTeam();
        runOnUiThread(()->{
            createTable();
            setMainMenuBtn();
            setWinnerTeamText();
            setWinnerTeamImage();
        });

    }
    private void createTable(){

        if (endGameTable.getChildCount() != 0) {
            return;
        }

        TableRow headerRow = new TableRow(this);
        String[] headers = {getString(R.string.number_column), getString(R.string.name_column)
                , getString(R.string.role_column), getString(R.string.win_loss_column)
                ,getString(R.string.alive_dead_column), getString(R.string.causes_of_death_column)};
        for (String text : headers) {
            TextView textView = new TextView(this);
            textView.setText(text);
            textView.setPadding(16, 16, 16, 16);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setTypeface(null, Typeface.BOLD);
            headerRow.addView(textView);
        }
        endGameTable.addView(headerRow);

        for (Player player : allPlayers) {
            TableRow tableRow = new TableRow(this);

            tableRow.addView(createTextView(String.format(Locale.ROOT,"%d", player.getNumber())));
            tableRow.addView(createTextView(player.getName()));
            tableRow.addView(createTextView(player.getRole().getTemplate().getName()));

            String winStatus;
            switch (player.getWinStatus()){
                case WON:
                    winStatus = getString(R.string.won);
                    break;
                case LOST:
                    winStatus = getString(R.string.lost);
                    break;
                case TIED:
                    winStatus = getString(R.string.tied);
                    break;
                default:
                    winStatus = "unknown";
                    break;
            }
            tableRow.addView(createTextView(winStatus));
            tableRow.addView(createTextView(player.getDeathProperties().isAlive() ? getString(R.string.alive) : getString(R.string.dead)));
            tableRow.addView(createTextView(player.getDeathProperties().getCausesOfDeathAsString()));

            endGameTable.addView(tableRow);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 16, 8, 16);
        textView.setTextColor(Color.WHITE);
        return textView;
    }

    private void setMainMenuBtn(){
        mainMenuBtn.setOnClickListener(v->{
            goMainMenu();
        });
    }

    private void setWinnerTeamText() {

        String winnerTeamStr;

        if (winningTeam == null) {
            winnerTeamStr = getString(R.string.winner_draw);
        } else {
            String teamKey =  TextManager.getInstance().enumToStringXmlPrefix(winningTeam.name(),"winner_team");

            int resId = getResources().getIdentifier(teamKey, "string", getPackageName());

            if (resId != 0) {
                winnerTeamStr = getString(R.string.winner_team_text).replace("{team}",getString(resId));
            } else {
                winnerTeamStr = teamKey;
            }
        }

        winnerTeamText.setText(winnerTeamStr);


    }

    private void setWinnerTeamImage(){
        Drawable image;

        if(winningTeam == null){
            image = AppCompatResources.getDrawable(this,R.drawable.winner_draw);
        }

        else{

            switch (winningTeam){
                case FOLK:
                    image = AppCompatResources.getDrawable(this,R.drawable.winner_folk);
                    break;

                case CORRUPTER:
                    image = AppCompatResources.getDrawable(this,R.drawable.winner_corrupt);
                    break;

                default:
                    image = AppCompatResources.getDrawable(this,R.drawable.winner_draw);
                    break;
            }
        }

        winnerTeamImage.setImageDrawable(image);
    }

    private void goMainMenu(){
        InstanceClearer.clearInstances();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
