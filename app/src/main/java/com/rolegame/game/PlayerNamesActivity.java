package com.rolegame.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.managers.SceneManager;
import com.rolegame.game.services.StartGameService;

public class PlayerNamesActivity extends AppCompatActivity {
    StartGameService startGameService;
    SceneManager sceneManager;

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

        playerCountText.setText(Integer.toString(startGameService.getPlayerCount()));

        minusBtn.setOnClickListener(v -> playerCountText.setText(Integer.toString(startGameService.decreasePlayerCount())));
        plusBtn.setOnClickListener(v -> playerCountText.setText(Integer.toString(startGameService.increasePlayerCount())));
    }
}
