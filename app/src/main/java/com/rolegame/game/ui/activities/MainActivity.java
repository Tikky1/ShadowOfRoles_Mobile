package com.rolegame.game.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.rolegame.game.ui.alerts.QuitAlert;
import com.rolegame.game.R;
import com.rolegame.game.managers.SceneManager;
import com.rolegame.game.ui.fragments.fullscreen.GameGuideFragment;

public class MainActivity extends BaseActivity{

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
        gameGuideBtn.setOnClickListener(v -> {
            GameGuideFragment gameGuideFragment = new GameGuideFragment();
            gameGuideFragment.show(getSupportFragmentManager(), "Game Guide");
        });
        creditsBtn.setOnClickListener(v -> openActivity(CreditsActivity.class));
        quitBtn.setOnClickListener(v -> {
            QuitAlert quitAlert = new QuitAlert(()->{
                MainActivity.this.finish();
                finishAffinity();
            });
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
