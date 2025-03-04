package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kankangames.shadowofroles.R;

public class ContactActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageButton mailButton = findViewById(R.id.mailBtn);
        ImageButton gitButton = findViewById(R.id.gitBtn);
        ImageButton playStoreButton = findViewById(R.id.storeBtn);
        TextView contactText = findViewById(R.id.contactText);

        contactText.setText(R.string.contact_text);

        gitButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Tikky1/ShadowOfRoles_Mobile/issues"));
            startActivity(intent);
        });

        mailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kankangames48@gmail.com"});

            startActivity(intent);

        });

        playStoreButton.setOnClickListener(v -> {

        });

    }
}
