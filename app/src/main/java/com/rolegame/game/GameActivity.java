package com.rolegame.game;

import static com.rolegame.game.gamestate.Time.*;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.gamestate.Time;
import com.rolegame.game.managers.GameScreenImageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityType;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.StartGameService;

public class GameActivity extends AppCompatActivity {

    private GameService gameService;
    private RecyclerView alivePlayersView;

    private TextView timeText;

    private TextView nameText;

    private TextView numberText;
    private TextView roleText;

    private Button passTurnButton;
    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameService = StartGameService.getInstance().getGameService();

        alivePlayersView = findViewById(R.id.alivePlayersView);
        timeText = findViewById(R.id.timeText);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        roleText = findViewById(R.id.roleText);
        passTurnButton = findViewById(R.id.pass_turn_button);
        backgroundImage = findViewById(R.id.backgroundImageGame);


        setTimeText();
        setNameText();
        setNumberText();
        setRoleText();
        setAlivePlayersView();
        setPassTurnButtonOnClicked();

    }

    private void setTimeText(){
        String template = gameService.getTimeService().getTime() != Time.NIGHT ?
                getString(R.string.day) :
                getString(R.string.night);

        template = String.format(template, gameService.getTimeService().getDayCount());
        timeText.setText(template);
    }

    private void setNameText(){
        Player currentPlayer = gameService.getCurrentPlayer();
        String template = getString(R.string.player_name);
        template = template
                .replace("{playerName}", currentPlayer.getName());
        nameText.setText(template);
    }

    private void setNumberText(){
        Player currentPlayer = gameService.getCurrentPlayer();
        String template = getString(R.string.player_number);
        template = template
                .replace("{playerNumber}", currentPlayer.getNumber()+"");
        numberText.setText(template);
    }

    private void setAlivePlayersView(){
        alivePlayersView.removeAllViews();
        PlayersViewAdapter playersViewAdapter = new PlayersViewAdapter(gameService.getTimeService().getTime(), gameService.getCurrentPlayer());
        playersViewAdapter.setPlayers(gameService.getAlivePlayers());
        alivePlayersView.setAdapter(playersViewAdapter);
        alivePlayersView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setRoleText(){
        Player currentPlayer = gameService.getCurrentPlayer();
        roleText.setText(currentPlayer.getRole().getTemplate().getName());
    }

    private void setPassTurnButtonOnClicked(){
        passTurnButton.setOnClickListener(v -> {
            if(gameService.getCurrentPlayer().getRole().getChoosenPlayer()==null){

                AbilityType abilityType = gameService.getCurrentPlayer().getRole().getTemplate().getAbilityType();
                if(gameService.getTimeService().getTime() == Time.VOTING||
                        (gameService.getTimeService().getTime() == Time.NIGHT &&
                                !(abilityType == AbilityType.NO_ABILITY|| abilityType == AbilityType.PASSIVE))){

                    // Alert will be shown
                }


            }

            //passTurnPane.setVisible(true);
            gameService.sendVoteMessages();

            if(gameService.passTurn()){

                toggleDayNightCycleUI();

            }
//            switch (gameService.getTimeService().getTime()){
//                case DAY -> setStyleImage(passTurnPane,"day");
//                case VOTING -> setStyleImage(passTurnPane,"vote");
//                case NIGHT -> setStyleImage(passTurnPane,"night");
//            }
            changePlayerUI();

        });
    }


    /**
     *
     * @param context
     * @return true if cancel clicked, false otherwise
     */
    private boolean showPassTurnAlert(Context context) {
        boolean[] result = {true};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("You are passing your turn are you sure?")
                .setMessage("Pass")
                .setCancelable(false)
                .setPositiveButton("Pass Turn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result[0] = false;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result[0] = true;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return result[0];
    }


    private void changePlayerUI(){
        setNameText();
        setNumberText();
        setRoleText();
        setAlivePlayersView();
    }


    private void setBackgroundImage(){
        GameScreenImageManager gameScreenImageManager = GameScreenImageManager.getInstance(this);
        Drawable image;
        switch (gameService.getTimeService().getTime()){
            case DAY:
                image = gameScreenImageManager.nextDayImage();
                break;
            case VOTING:
                image = gameScreenImageManager.nextVotingImage();
                break;
            case NIGHT:
                image = gameScreenImageManager.nextNightImage();
                break;
            default:
                image = null;
                break;

        }
        backgroundImage.setImageDrawable(image);
    }

    private void toggleDayNightCycleUI(){
        setBackgroundImage();
        setTimeText();
    }
}
