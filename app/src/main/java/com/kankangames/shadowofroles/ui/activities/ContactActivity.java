package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kankangames.shadowofroles.R;

public class ContactActivity extends ImageChangingActivity {
    private ImageView backgroundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageButton mailButton = findViewById(R.id.mailBtn);
        ImageButton gitButton = findViewById(R.id.gitBtn);
        ImageButton playStoreButton = findViewById(R.id.storeBtn);
        TextView contactText = findViewById(R.id.contactText);
        backgroundImage = findViewById(R.id.contact_background_image);
        backgroundImage.setImageDrawable(sceneManager.nextImage());

        backgroundImage.setImageDrawable(sceneManager.nextImage());

        contactText.setText(R.string.contact_text);

        gitButton.setOnClickListener(v -> openLink("https://github.com/Tikky1/ShadowOfRoles_Mobile/issues"));

        mailButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kankangames48@gmail.com"});

            startActivity(intent);

        });

        playStoreButton.setOnClickListener(v -> {
            openLink("https://play.google.com/store/apps/details?id=com.kankangames.shadowofroles");
        });

    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }

    private void openLink(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}
