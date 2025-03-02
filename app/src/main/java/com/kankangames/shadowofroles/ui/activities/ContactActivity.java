package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.LanguageManager;

public class ContactActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageButton mailButton = findViewById(R.id.mailBtn);
        ImageButton gitButton = findViewById(R.id.gitBtn);
        ImageButton playStoreButton = findViewById(R.id.storeBtn);
        TextView contactText = findViewById(R.id.contactText);

        contactText.setText(LanguageManager.getInstance().getText("contact_text"));

        gitButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Tikky1/ShadowOfRoles_Mobile/issues"));
            startActivity(intent);
        });

        mailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kankangames48@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Konu Başlığı");
            intent.putExtra(Intent.EXTRA_TEXT, "Merhaba, bu bir test e-postası.");

            if(intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        });

        playStoreButton.setOnClickListener(v -> {

        });

    }
}
