package com.rolegame.game.ui.activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.rolegame.game.R;
import com.rolegame.game.gamestate.WinnerTeam;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.StartGameService;

public class GameEndActivity extends BaseActivity {

    private TableLayout endGameTable;

    private Button mainMenuBtn;

    private TextView winnerTeamText;
    private ImageView winnerTeamImage;

    private GameService gameService;

    private WinnerTeam winnerTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        endGameTable = findViewById(R.id.end_game_table);
        mainMenuBtn = findViewById(R.id.go_back_main_button);
        winnerTeamText = findViewById(R.id.winner_team_text);
        winnerTeamImage = findViewById(R.id.winner_team_image);

        gameService = StartGameService.getInstance().getGameService();
        winnerTeam = gameService.getFinishGameService().getHighestPriorityWinningTeam();

        createTable();
        setMainMenuBtn();
        setWinnerTeamText();
        setWinnerTeamImage();
    }



    private void createTable(){
        TableRow headerRow = new TableRow(this);
        String[] headers = {"Number", "Name", "Role", "Win/Loss","Alive/Dead", "Cause Of Deaths"};
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

            tableRow.addView(createTextView(String.format("%d", player.getNumber())));
            tableRow.addView(createTextView(player.getName()));
            tableRow.addView(createTextView(player.getRole().getTemplate().getName()));
            tableRow.addView(createTextView(player.isHasWon() ? "Won" : "Lost"));
            tableRow.addView(createTextView(player.isAlive() ? "Alive" : "Dead"));
            tableRow.addView(createTextView(player.getCausesOfDeathAsString()));

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

        if (winnerTeam == null) {
            winnerTeamStr = getString(R.string.winner_draw);
        } else {
            String teamKey = "winner_team_" + LanguageManager.getInstance().enumToStringXml(winnerTeam.name());

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

        if(winnerTeam == null){
            image = AppCompatResources.getDrawable(this,R.drawable.winner_draw);
        }

        else{

            switch (winnerTeam){
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
