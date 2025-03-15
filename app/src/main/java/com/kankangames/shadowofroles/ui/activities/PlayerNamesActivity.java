package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.player.AIPlayer;
import com.kankangames.shadowofroles.models.player.HumanPlayer;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.adapters.PlayerNamesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerNamesActivity extends ImageChangingActivity{

    private List<String> playerNames;
    private List<Boolean> isPlayersAI;
    private StartGameService startGameService;
    private RecyclerView playerNamesContainer;
    private PlayerNamesAdapter adapter;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_names);

        startGameService = StartGameService.getInstance();
        startGameService.setGameMode(GameMode.SINGLE_DEVICE);
        backgroundImage = findViewById(R.id.backgroundImage);
        backgroundImage.setImageDrawable(sceneManager.nextImage());

        Button minusBtn = findViewById(R.id.minusBtn);
        Button plusBtn = findViewById(R.id.plusBtn);
        TextView playerCountText = findViewById(R.id.playerCountText);
        Button startGameBtn = findViewById(R.id.startGameBtn);
        playerNamesContainer = findViewById(R.id.playerNamesContainer);

        startGameService.setPlayerCountMin();

        playerNames = new ArrayList<>();
        isPlayersAI = new ArrayList<>();

        adapter = new PlayerNamesAdapter(playerNames, isPlayersAI);
        playerNamesContainer.setLayoutManager(new LinearLayoutManager(this));
        playerNamesContainer.setAdapter(adapter);

        for (int i = 1; i <= startGameService.MIN_PLAYER_COUNT; i++) {
            playerNames.add(String.format(Locale.ROOT,getString(R.string.player), i));
            isPlayersAI.add(false);
        }
        adapter.notifyDataSetChanged();

        playerCountText.setText(String.format(Locale.ROOT,"%d",startGameService.getPlayerCount()));

        plusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount() < startGameService.MAX_PLAYER_COUNT) {
                playerCountText.setText(String.format(Locale.ROOT,"%d",startGameService.increasePlayerCount()));
                playerNames.add(String.format(Locale.ROOT,getString(R.string.player), startGameService.getPlayerCount()));
                isPlayersAI.add(false);
                adapter.notifyItemInserted(playerNames.size() - 1);
                playerNamesContainer.post(() -> playerNamesContainer.smoothScrollToPosition(adapter.getItemCount() - 1));

            }
        });

        minusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount() > startGameService.MIN_PLAYER_COUNT) {
                playerCountText.setText(String.format(Locale.ROOT,"%d",startGameService.decreasePlayerCount()));
                int lastIndex = playerNames.size() - 1;
                playerNames.remove(lastIndex);
                isPlayersAI.remove(lastIndex);
                adapter.notifyItemRemoved(lastIndex);
                playerNamesContainer.post(() -> playerNamesContainer.smoothScrollToPosition(adapter.getItemCount() - 1));
            }
        });

        startGameBtn.setOnClickListener(v -> {
            int playerCount = startGameService.getPlayerCount();
            ArrayList<Player> players = new ArrayList<>(playerCount);

            boolean isHumanPlayerExist = false;

            for (int i = 0; i < playerCount; ++i) {


                String playerNameTemplate = playerNames.get(i);
                Boolean isAI = isPlayersAI.get(i);
                int number = i+1;
                String playerName = playerNameTemplate.isBlank() ?
                        String.format(Locale.ROOT,getString(R.string.player), number) : playerNameTemplate;

                if (isAI) {
                    players.add(new AIPlayer(number, playerName));
                } else {
                    players.add(new HumanPlayer(number, playerName));
                    isHumanPlayerExist = true;
                }

            }

            if (!isHumanPlayerExist) {
                Toast.makeText(this, getString(R.string.all_players_ai_alert), Toast.LENGTH_LONG).show();
                return;
            }

            startGameService.initializeGameService(players);

            Intent intent = new Intent(this, SingleDeviceGameActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }
}
