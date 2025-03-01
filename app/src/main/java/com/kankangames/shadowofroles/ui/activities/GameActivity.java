package com.kankangames.shadowofroles.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.ui.adapters.PlayersViewAdapter;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.GameScreenImageManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.services.GameService;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.alerts.GoToMainAlert;
import com.kankangames.shadowofroles.ui.fragments.RoleBookFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.AnnouncementsFragment;
import com.kankangames.shadowofroles.ui.fragments.GraveyardFragment;
import com.kankangames.shadowofroles.ui.fragments.MessageFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.PassTurnFragment;

public class GameActivity extends BaseActivity{

    private GameService gameService;
    private RecyclerView alivePlayersView;

    private TextView timeText;
    private TextView nameText;
    private TextView numberText;
    private TextView roleText;

    private ImageButton announcementsButton;
    private ImageButton graveyardButton;
    private ImageButton roleBookButton;
    private Button passTurnButton;
    private ImageView backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        gameService = StartGameService.getInstance().getGameService();

        initializeViews();

        createPassTurnDialog();
        setPassTurnButtonOnClicked();
        setTimeText();
        setBackgroundImage();
        setImageButtonOnClicked();


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                GoToMainAlert goToMainAlert = new GoToMainAlert(()->{
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                });
                goToMainAlert.show(getSupportFragmentManager(), "Go to main menu");
            }
        });

    }


    private void initializeViews(){
        alivePlayersView = findViewById(R.id.alivePlayersView);
        timeText = findViewById(R.id.timeText);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        roleText = findViewById(R.id.roleText);
        passTurnButton = findViewById(R.id.pass_turn_button);
        backgroundImage = findViewById(R.id.backgroundImageGame);
        announcementsButton = findViewById(R.id.announcementsBtn);
        graveyardButton = findViewById(R.id.gravestoneBtn);
        roleBookButton = findViewById(R.id.roleBookBtn);
    }
    private void setImageButtonOnClicked(){
        announcementsButton.setOnClickListener(v -> {
            MessageFragment messageFragment = new MessageFragment(
                    gameService.getMessageService().getMessages(), gameService.getCurrentPlayer());

            messageFragment.show(getSupportFragmentManager(), "Messages");
        });

        graveyardButton.setOnClickListener(v -> {
            GraveyardFragment graveyardFragment = new GraveyardFragment(
                    gameService.getDeadPlayers());

            graveyardFragment.show(getSupportFragmentManager(), "Graveyard");
        });

        roleBookButton.setOnClickListener(v->{
            RoleBookFragment roleBookFragment = new RoleBookFragment();
            roleBookFragment.show(getSupportFragmentManager(), "Role Book");
        });
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
        PlayersViewAdapter playersViewAdapter = new PlayersViewAdapter(gameService.getTimeService().getTime(), gameService.getCurrentPlayer(), this);
        playersViewAdapter.setPlayers(gameService.getAlivePlayers());
        alivePlayersView.setAdapter(playersViewAdapter);
        alivePlayersView.setLayoutManager(new LinearLayoutManager(this));

        int maxHeight = getResources().getDimensionPixelSize(R.dimen.max_recycler_view_height);
        ViewGroup.LayoutParams params = alivePlayersView.getLayoutParams();
        params.height = maxHeight;
        alivePlayersView.setLayoutParams(params);
        alivePlayersView.setItemViewCacheSize(gameService.getAlivePlayers().size());
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

            boolean isTimeChanged = gameService.passTurn();

            createPassTurnDialog();

            if(isTimeChanged){

                changeTimeUI();


            }


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
                .setPositiveButton("Pass Turn", (dialog, which) -> result[0] = false)
                .setNegativeButton("Cancel", (dialog, which) -> result[0] = true);

        AlertDialog dialog = builder.create();
        dialog.show();
        return result[0];
    }


    private void changePlayerUI(){
        setNameText();
        setNumberText();
        setRoleText();
        setAlivePlayersView();
        passTurnButton.setClickable(true);
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

    private void changeTimeUI(){

        if(gameService.getFinishGameService().isGameFinished()){
            Intent intent = new Intent(this, GameEndActivity.class);
            startActivity(intent);
        }

        setBackgroundImage();
        setTimeText();

        switch (gameService.getTimeService().getTime()){
            case DAY:
            case NIGHT:
                createAnnouncementsDialog();
                break;
            case VOTING:
                break;
        }


    }

    private void createPassTurnDialog(){
        passTurnButton.setClickable(false);
        PassTurnFragment passTurnFragment = new PassTurnFragment(this::changePlayerUI);
        passTurnFragment.setPlayerName(gameService.getCurrentPlayer().getName());


        GameScreenImageManager gameScreenImageManager = GameScreenImageManager.getInstance(this);
        Drawable image;
        switch (gameService.getTimeService().getTime()){
            case DAY:
                image = gameScreenImageManager.nextDayPassingTurnImage();
                break;
            case VOTING:
                image = gameScreenImageManager.nextVotingPassingTurnImage();
                break;
            case NIGHT:
                image = gameScreenImageManager.nextNightPassingTurnImage();
                break;
            default:
                image = null;
                break;

        }
        passTurnFragment.setFragmentBackground(image);

        passTurnFragment.show(getSupportFragmentManager(), "Pass Turn");
    }

    private void createAnnouncementsDialog(){
        AnnouncementsFragment announcementsFragment = new AnnouncementsFragment(()->{});
        announcementsFragment.setAnnouncementsAndTimeService(gameService.getMessageService().getMessages(),
                gameService.getTimeService());
        announcementsFragment.setDayText(gameService.getTimeService().getTimeAndDay());
        announcementsFragment.show(getSupportFragmentManager(), "Start Day Announcements");
    }
}
