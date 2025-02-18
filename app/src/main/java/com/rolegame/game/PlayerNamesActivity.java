package com.rolegame.game;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.managers.SceneManager;
import com.rolegame.game.models.player.AIPlayer;
import com.rolegame.game.models.player.HumanPlayer;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.services.StartGameService;

import java.util.ArrayList;

public class PlayerNamesActivity extends AppCompatActivity {
    StartGameService startGameService;
    SceneManager sceneManager;

    LinearLayout playerNamesContainer;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_names);

        startGameService = StartGameService.getInstance();
        sceneManager = SceneManager.getInstance(this);

        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageDrawable(sceneManager.nextImage());

        Button minusBtn = findViewById(R.id.minusBtn);
        Button plusBtn = findViewById(R.id.plusBtn);
        TextView playerCountText = findViewById(R.id.playerCountText);
        Button startGameBtn = findViewById(R.id.startGameBtn);
        playerNamesContainer = findViewById(R.id.playerNamesContainer);

        startGameService.setPlayerCountMin();

        playerCountText.setText(Integer.toString(startGameService.getPlayerCount()));

        for (int i = 1; i <= startGameService.MIN_PLAYER_COUNT; i++) {
            addPlayerEditText(i);
        }
        plusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount()<startGameService.MAX_PLAYER_COUNT) {
                playerCountText.setText(Integer.toString(startGameService.increasePlayerCount()));

                addPlayerEditText(startGameService.getPlayerCount());
            }

        });
        minusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount()>startGameService.MIN_PLAYER_COUNT) {
                playerCountText.setText(Integer.toString(startGameService.decreasePlayerCount()));
                int lastIndex = playerNamesContainer.getChildCount() - 1;
                playerNamesContainer.removeViewAt(lastIndex);
            }
        });

        startGameBtn.setOnClickListener(v -> {
            int playerCount = startGameService.getPlayerCount();
            ArrayList<Player> players = new ArrayList<>(playerCount);

            for(int i=0;i<playerCount;++i){
                LinearLayout layout = (LinearLayout) playerNamesContainer.getChildAt(0);
                CheckBox checkBox = (CheckBox) layout.getChildAt(1);
                EditText editText = (EditText) layout.getChildAt(0);

                String playerName = editText.getText().toString();
                if(checkBox.isSelected()){
                    players.add(new AIPlayer(i+1, playerName));
                }
                else{
                    players.add(new HumanPlayer(i+1, playerName));
                }
            }
            Toast.makeText(this, "Game is Starting", Toast.LENGTH_SHORT).show();
            startGameService.initializeGameService(players);

            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }

    private void addPlayerEditText(int playerNumber) {
        LinearLayout layout = new LinearLayout(this);
        EditText editText = new EditText(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        String string = getString(R.string.player) + playerNumber;

        editText.setText(string);
        editText.setPadding(10, 10, 10, 10);
        editText.setTextColor(Color.WHITE);

        editText.setLayoutParams(params);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setButtonTintList(ColorStateList.valueOf(Color.WHITE));

        layout.addView(editText);
        layout.addView(checkBox);
        playerNamesContainer.addView(layout);
    }

}
