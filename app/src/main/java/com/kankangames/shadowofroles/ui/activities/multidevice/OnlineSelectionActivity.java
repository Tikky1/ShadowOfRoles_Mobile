package com.kankangames.shadowofroles.ui.activities.multidevice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;

public class OnlineSelectionActivity extends BaseActivity {

    private EditText nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_selection);


        Button hostGameBtn = findViewById(R.id.host_game_btn);
        Button searchGameBtn = findViewById(R.id.search_game_btn);
        nameText = findViewById(R.id.online_player_name);

        hostGameBtn.setOnClickListener(v -> {
            initializeClient(GameHostingActivity.class);
        });

        searchGameBtn.setOnClickListener(v -> {
            initializeClient(ListOnlineGamesActivity.class);
        });

    }

    private void initializeClient(Class<?> cls){
        String playerName = nameText.getText().toString();
        if(playerName.isEmpty()){
            playerName = Build.BRAND + " " + Build.MODEL;
        }
        Client client = new Client(playerName);
        ClientManager.getInstance().setClient(client);

        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
