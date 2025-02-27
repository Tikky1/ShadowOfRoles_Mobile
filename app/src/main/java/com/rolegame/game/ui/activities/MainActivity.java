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

public class MainActivity extends ImageChangingActivity {

    private ImageView backgroundImage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button startGameBtn = findViewById(R.id.startGameBtn);
        Button gameGuideBtn = findViewById(R.id.gameGuideBtn);
        Button feedbackBtn = findViewById(R.id.feedbackBtn);
        Button creditsBtn = findViewById(R.id.creditsBtn);
        Button quitBtn = findViewById(R.id.quitBtn);
        backgroundImage = findViewById(R.id.backgroundImage);

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
            quitAlert.show(getSupportFragmentManager(), "Quit Alert");
        });

        backgroundImage.setImageDrawable(sceneManager.getCurrentImage());

    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }


    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        launcher.launch(intent);
    }





}
