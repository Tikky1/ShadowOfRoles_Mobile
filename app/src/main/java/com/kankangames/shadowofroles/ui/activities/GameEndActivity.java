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
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.services.GameService;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.ChillGuyFragment;

import java.util.Locale;

public class GameEndActivity extends BaseActivity{

    private TableLayout endGameTable;

    private Button mainMenuBtn;

    private TextView winnerTeamText;
    private ImageView winnerTeamImage;

    private GameService gameService;

    private WinningTeam winningTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_end);

        gameService = StartGameService.getInstance().getGameService();

        endGameTable = findViewById(R.id.end_game_table);
        mainMenuBtn = findViewById(R.id.go_back_main_button);
        winnerTeamText = findViewById(R.id.winner_team_text);
        winnerTeamImage = findViewById(R.id.winner_team_image);

        winningTeam = gameService.getFinishGameService().getHighestPriorityWinningTeam();

        boolean chillGuyExist = createChillGuyAlert();

        if(!chillGuyExist){
            setActivity();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(GameEndActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean createChillGuyAlert(){
        Player chillGuyPlayer = gameService.getFinishGameService().getChillGuyPlayer();
        if(chillGuyPlayer != null){
            ChillGuyFragment chillGuyFragment = new ChillGuyFragment(this::setActivity, chillGuyPlayer, gameService.getFinishGameService());
            chillGuyFragment.show(getSupportFragmentManager(), "Chill Guy Alert");
            return true;
        }
        return false;
    }

    private void setActivity(){
        createTable();
        setMainMenuBtn();
        setWinnerTeamText();
        setWinnerTeamImage();
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

        for (Player player : gameService.getAllPlayers()) {
            TableRow tableRow = new TableRow(this);

            tableRow.addView(createTextView(String.format(Locale.ROOT,"%d", player.getNumber())));
            tableRow.addView(createTextView(player.getName()));
            tableRow.addView(createTextView(player.getRole().getTemplate().getName()));
            tableRow.addView(createTextView(player.isHasWon() ? getString(R.string.won) : getString(R.string.lost)));
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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


}
