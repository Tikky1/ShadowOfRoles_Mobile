package com.kankangames.shadowofroles.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.settings.Language;
import com.kankangames.shadowofroles.utils.managers.LanguageManager;
import com.kankangames.shadowofroles.utils.managers.SettingsManager;
import com.kankangames.shadowofroles.game.models.settings.UserSettings;
import com.kankangames.shadowofroles.ui.adapters.LanguageSelectAdapter;

import java.util.OptionalInt;
import java.util.stream.IntStream;

public class SettingsActivity extends ImageChangingActivity{

    ImageView background;

    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        background=findViewById(R.id.backgroundImage3);
        background.setImageDrawable(sceneManager.nextImage());

        versionTextView = findViewById(R.id.versionTextview);
        version();

        AppCompatButton closeButton = findViewById(R.id.close_settings_button);
        closeButton.setOnClickListener(v -> finish());

        initLanguageSpinner();
        initUsername();
    }

    @Override
    protected ImageView getBackgroundImage() {
        return findViewById(R.id.backgroundImage3);
    }

    private void initLanguageSpinner(){
        LanguageManager languageManager = LanguageManager.getInstance();
        Spinner spinner = findViewById(R.id.lang_spinner);
        LanguageSelectAdapter languageSelectAdapter = new LanguageSelectAdapter(this);
        spinner.setAdapter(languageSelectAdapter);

        Language[] languages = Language.values();
        OptionalInt index = IntStream.range(0, languages.length).
                filter(i-> languages[i] == languageManager.getSavedLanguage())
                .findFirst();
        index.ifPresent(spinner::setSelection);


        Button selectLangBtn = findViewById(R.id.lang_btn);

        selectLangBtn.setOnClickListener(v -> {

            Language lang = (Language) spinner.getSelectedItem();
            languageManager.setLocale(this,lang);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

    }

    private void initUsername(){
        UserSettings settings = SettingsManager.getSettings(this);
        EditText userNameText = findViewById(R.id.username_edit_text);
        userNameText.setText(settings.username());
        Button selectBtn = findViewById(R.id.username_btn);
        selectBtn.setOnClickListener(v ->
        {
            settings.setUsername(userNameText.getText().toString());
            SettingsManager.saveSettings(SettingsActivity.this, settings);
            userNameText.setTooltipText(settings.username());
        });
        userNameText.setTooltipText(settings.username());
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
