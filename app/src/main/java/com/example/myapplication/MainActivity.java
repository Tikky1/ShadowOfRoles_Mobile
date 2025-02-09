package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Butonları bağla
        Button startGameButton = findViewById(R.id.startGameButton);
        Button gameGuideButton = findViewById(R.id.gameGuideButton);
        Button achievementsButton = findViewById(R.id.achievementsButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button creditsButton = findViewById(R.id.creditsButton);
        Button exitButton = findViewById(R.id.exitButton);

        // Butonlara tıklama olaylarını ekle
        startGameButton.setOnClickListener(v -> openActivity(PlayerNamesActivity.class));
        gameGuideButton.setOnClickListener(v -> openActivity(GameGuideActivity.class));
        achievementsButton.setOnClickListener(v -> openActivity(AchievementsActivity.class));
        settingsButton.setOnClickListener(v -> openActivity(SettingsActivity.class));
        creditsButton.setOnClickListener(v -> openActivity(CreditsActivity.class));

        // Çıkış butonu
        exitButton.setOnClickListener(v -> finish());
    }

    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
