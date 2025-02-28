package com.rolegame.game.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.managers.SceneManager;
import com.rolegame.game.models.player.AIPlayer;
import com.rolegame.game.models.player.HumanPlayer;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.services.StartGameService;
import com.rolegame.game.ui.adapters.PlayerNamesAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlayerNamesActivity extends ImageChangingActivity{

    private List<String> playerNames;
    private List<Boolean> isPlayersAI;
    private StartGameService startGameService;
    private RecyclerView playerNamesContainer;
    private PlayerNamesAdapter adapter;
    private ImageView backgroundImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_names);

        startGameService = StartGameService.getInstance();

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
            playerNames.add("Player " + i);
            isPlayersAI.add(false);
        }
        adapter.notifyDataSetChanged();

        playerCountText.setText(Integer.toString(startGameService.getPlayerCount()));

        plusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount() < startGameService.MAX_PLAYER_COUNT) {
                playerCountText.setText(Integer.toString(startGameService.increasePlayerCount()));
                playerNames.add("Player " + startGameService.getPlayerCount());
                isPlayersAI.add(false);
                adapter.notifyItemInserted(playerNames.size() - 1);
            }
        });

        minusBtn.setOnClickListener(v -> {
            if (startGameService.getPlayerCount() > startGameService.MIN_PLAYER_COUNT) {
                playerCountText.setText(Integer.toString(startGameService.decreasePlayerCount()));
                int lastIndex = playerNames.size() - 1;
                playerNames.remove(lastIndex);
                isPlayersAI.remove(lastIndex);
                adapter.notifyItemRemoved(lastIndex);
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
                String playerName = playerNameTemplate.isBlank() ? "Player " + number : playerNameTemplate;

                if (isAI) {
                    players.add(new AIPlayer(number, playerName));
                } else {
                    players.add(new HumanPlayer(number, playerName));
                    isHumanPlayerExist = true;
                }

            }

            if (!isHumanPlayerExist) {
                Toast.makeText(this, "All Players Cannot be AI Player!", Toast.LENGTH_LONG).show();
                return;
            }

            startGameService.initializeGameService(players);

            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }
}
