package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.kankangames.shadowofroles.ui.alerts.QuitAlert;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.GameGuideFragment;

public class MainActivity extends ImageChangingActivity {

    private ImageView backgroundImage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button startGameBtn = findViewById(R.id.startGameBtn);
        Button gameGuideBtn = findViewById(R.id.gameGuideBtn);
        Button contactBtn = findViewById(R.id.contactBtn);
        Button creditsBtn = findViewById(R.id.creditsBtn);
        Button quitBtn = findViewById(R.id.quitBtn);
        backgroundImage = findViewById(R.id.backgroundImage);

        // Click Listeners
        startGameBtn.setOnClickListener(v -> openActivity(PlayerNamesActivity.class));
        contactBtn.setOnClickListener(v ->{openActivity(ContactActivity.class);});
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
