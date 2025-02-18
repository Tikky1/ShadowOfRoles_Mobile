package com.rolegame.game;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.services.GameService;
import com.rolegame.game.services.StartGameService;

public class GameActivity extends AppCompatActivity {

    private GameService gameService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameService = StartGameService.getInstance().getGameService();
    }
}
