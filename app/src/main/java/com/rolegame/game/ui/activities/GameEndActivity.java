package com.rolegame.game.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.R;
import com.rolegame.game.gamestate.WinnerTeam;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.StartGameService;

public class GameEndActivity extends AppCompatActivity {

    private TableLayout endGameTable;

    private Button mainMenuBtn;

    private TextView winnerTeamText;

    private GameService gameService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        endGameTable = findViewById(R.id.end_game_table);
        mainMenuBtn = findViewById(R.id.go_back_main_button);
        winnerTeamText = findViewById(R.id.winner_team_text);

        gameService = StartGameService.getInstance().getGameService();

        createTable();
        setMainMenuBtn();
        setWinnerTeamText();
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

            TextView idView = new TextView(this);
            idView.setText(String.format("%d", player.getNumber()));
            idView.setPadding(8, 16, 8, 16);
            tableRow.addView(idView);

            TextView nameView = new TextView(this);
            nameView.setText(player.getName());
            nameView.setPadding(8, 16, 8, 16);
            tableRow.addView(nameView);

            TextView roleView = new TextView(this);
            roleView.setText(player.getRole().getTemplate().getName());
            roleView.setPadding(8, 16, 8, 16);
            tableRow.addView(roleView);

            TextView winStatusView = new TextView(this);
            winStatusView.setText(player.isHasWon() ? "Won" : "Lost");
            winStatusView.setPadding(8, 16, 8, 16);
            tableRow.addView(winStatusView);

            TextView aliveStatusView = new TextView(this);
            aliveStatusView.setText(player.isAlive() ? "Alive" : "Dead");
            aliveStatusView.setPadding(8, 16, 8, 16);
            tableRow.addView(aliveStatusView);

            TextView causeOfDeaths = new TextView(this);
            causeOfDeaths.setText(String.format(player.getCausesOfDeathAsString()));
            causeOfDeaths.setPadding(8, 16, 8, 16);
            tableRow.addView(causeOfDeaths);

            endGameTable.addView(tableRow);
        }
    }

    private void setMainMenuBtn(){
        mainMenuBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void setWinnerTeamText() {
        String winnerTeamStr;
        WinnerTeam winnerTeam = gameService.getFinishGameService().getHighestPriorityWinningTeam();

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


}
