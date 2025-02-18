package com.rolegame.game;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.managers.SceneManager;

public class MainActivity extends AppCompatActivity {
    private Button startGameBtn;
    private Button gameGuideBtn;
    private Button feedbackBtn;
    private Button creditsBtn;
    private Button quitBtn;

    private SceneManager sceneManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startGameBtn = findViewById(R.id.startGameBtn);
        gameGuideBtn = findViewById(R.id.gameGuideBtn);
        feedbackBtn = findViewById(R.id.feedbackBtn);
        creditsBtn = findViewById(R.id.creditsBtn);
        quitBtn = findViewById(R.id.quitBtn);

        // Click Listeners
        startGameBtn.setOnClickListener(v -> openActivity(PlayerNamesActivity.class));
        feedbackBtn.setOnClickListener(v ->{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FurkanKirat/Shadow-of-Roles/issues"));
                    startActivity(browserIntent);
                });
        quitBtn.setOnClickListener(v -> {
            QuitAlert quitAlert = new QuitAlert();
            quitAlert.show(getSupportFragmentManager(), "quitAlert");
        });

        ImageView backgroundImage = findViewById(R.id.backgroundImage);
        sceneManager = SceneManager.getInstance(this);
        backgroundImage.setImageDrawable(sceneManager.nextImage());

    }

    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


}
