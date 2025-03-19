package com.kankangames.shadowofroles.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kankangames.shadowofroles.ui.activities.multidevice.OnlineSelectionActivity;
import com.kankangames.shadowofroles.ui.alerts.QuitAlert;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.GameGuideFragment;

public class MainActivity extends ImageChangingActivity {

    private ImageView backgroundImage;

    private TextView versionTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameBtn = findViewById(R.id.startGameBtn);
        Button multiDeviceBtn = findViewById(R.id.multiDeviceGameBtn);
        Button gameGuideBtn = findViewById(R.id.gameGuideBtn);
        Button contactBtn = findViewById(R.id.contactBtn);
        Button creditsBtn = findViewById(R.id.creditsBtn);
        Button quitBtn = findViewById(R.id.quitBtn);
        ImageButton settingsBtn = findViewById(R.id.settingsBtn);

//        ImageView cloudImage = findViewById(R.id.cloud_image_view);
        backgroundImage = findViewById(R.id.backgroundImage);
        versionTextView = findViewById(R.id.versionTextview);

        version();

        // Click Listeners
        startGameBtn.setOnClickListener(v -> openActivity(PlayerNamesActivity.class));
        contactBtn.setOnClickListener(v ->{openActivity(ContactActivity.class);});
        gameGuideBtn.setOnClickListener(v -> {
            GameGuideFragment gameGuideFragment = new GameGuideFragment();
            gameGuideFragment.show(getSupportFragmentManager(), getString(R.string.game_guide));
        });
        multiDeviceBtn.setOnClickListener(v -> openActivity(OnlineSelectionActivity.class));
        creditsBtn.setOnClickListener(v -> openActivity(CreditsActivity.class));
        quitBtn.setOnClickListener(v -> {
            QuitAlert quitAlert = new QuitAlert(()->{
                MainActivity.this.finish();
                finishAffinity();
            });
            quitAlert.show(getSupportFragmentManager(), "Quit Alert");
        });

        settingsBtn.setOnClickListener(v -> {
            openActivity(SettingsActivity.class);
        });

//        backgroundImage.setImageDrawable(sceneManager.getCurrentImage());
//
//        Animation animationBackground = AnimationUtils.loadAnimation(this, R.anim.background_move);
//        getBackgroundImage().startAnimation(animationBackground);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cloud_move);
////        cloudImage.startAnimation(animation);

    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }


    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        launcher.launch(intent);
    }
    private void version(){
        try {
            Context context = getApplicationContext();
            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            versionTextView.setText("Version: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }







}
