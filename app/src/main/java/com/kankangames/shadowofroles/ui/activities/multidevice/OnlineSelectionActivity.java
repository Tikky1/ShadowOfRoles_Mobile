package com.kankangames.shadowofroles.ui.activities.multidevice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.ImageChangingActivity;
import com.kankangames.shadowofroles.ui.activities.multidevice.lobby.GameHostingActivity;

import java.util.Locale;
import java.util.UUID;

public class OnlineSelectionActivity extends ImageChangingActivity {

    private EditText nameText;
    private ImageView backgroundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_selection);


        AppCompatButton closeButton = findViewById(R.id.close_online_selection_button);
        closeButton.setOnClickListener(v -> finish());

        Button hostGameBtn = findViewById(R.id.host_game_btn);
        Button searchGameBtn = findViewById(R.id.search_game_btn);
        backgroundImage = findViewById(R.id.backgroundImage);
        changeImage();
        nameText = findViewById(R.id.online_player_name);

        hostGameBtn.setOnClickListener(v -> {
            initializeClient(GameHostingActivity.class);
        });

        searchGameBtn.setOnClickListener(v -> {
            initializeClient(ListOnlineGamesActivity.class);
        });


    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }

    private void initializeClient(Class<?> cls){
        String playerName = nameText.getText().toString();
        if(playerName.isEmpty()){
            playerName = String.format(Locale.ROOT, getString(R.string.random_player_name),
                    UUID.randomUUID().toString().substring(0, 8)) ;
        }
        Client client = new Client(playerName);
        ClientManager.getInstance().setClient(client);

        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
