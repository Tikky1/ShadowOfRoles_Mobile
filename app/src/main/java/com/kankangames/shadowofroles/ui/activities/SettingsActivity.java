package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatButton;

import com.kankangames.shadowofroles.GameApplication;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.LanguageManager;
import com.kankangames.shadowofroles.ui.adapters.LanguageSelectAdapter;

public class SettingsActivity extends ImageChangingActivity{

    ImageView background;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        background=findViewById(R.id.backgroundImage3);
        background.setImageDrawable(sceneManager.nextImage());

        AppCompatButton closeButton = findViewById(R.id.close_settings_button);
        closeButton.setOnClickListener(v -> finish());

        setLanguageSpinner();
    }

    @Override
    protected ImageView getBackgroundImage() {
        return findViewById(R.id.backgroundImage3);
    }

    private void setLanguageSpinner(){
        LanguageManager languageManager = LanguageManager.getInstance();
        Spinner spinner = findViewById(R.id.lang_spinner);
        LanguageSelectAdapter languageSelectAdapter = new LanguageSelectAdapter(this);
        spinner.setAdapter(languageSelectAdapter);
        switch (languageManager.getSavedLanguage()){
            case "tr":
                spinner.setSelection(1);
                break;
            default:
                spinner.setSelection(0);
                break;
        }


        Button selectLangBtn = findViewById(R.id.choose_language_btn);

        selectLangBtn.setOnClickListener(v -> {
            String lang;
            if(spinner.getSelectedItem().equals("Türkçe")){
                lang = "tr";
            }
            else {
                lang = "en";
            }
            languageManager.setLocale(this,lang);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

    }



}
