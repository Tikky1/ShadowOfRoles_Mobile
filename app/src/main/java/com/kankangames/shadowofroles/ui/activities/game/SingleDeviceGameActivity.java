package com.kankangames.shadowofroles.ui.activities.game;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.game.models.gamestate.GameMode;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;
import com.kankangames.shadowofroles.ui.activities.GameEndActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.adapters.PlayersViewAdapter;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.ui.helper.GameScreenImageManager;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.services.SingleDeviceGameService;
import com.kankangames.shadowofroles.game.services.StartGameService;
import com.kankangames.shadowofroles.ui.alerts.GoToMainAlert;
import com.kankangames.shadowofroles.ui.fragments.RoleBookFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.AnnouncementsFragment;
import com.kankangames.shadowofroles.ui.fragments.GraveyardFragment;
import com.kankangames.shadowofroles.ui.fragments.MessageFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.PassTurnFragment;

import java.util.Locale;

public class SingleDeviceGameActivity extends BaseActivity {

    private SingleDeviceGameService gameService;
    private RecyclerView alivePlayersView;

    private TextView timeText, nameText, numberText, roleText;

    private ImageButton announcementsButton, graveyardButton, roleBookButton;
    private Button passTurnButton;
    private ImageView backgroundImage;
    private final GameMode gameMode = GameMode.SINGLE_DEVICE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        gameService = (SingleDeviceGameService) StartGameService.getInstance().getGameService();

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
                    Intent intent = new Intent(SingleDeviceGameActivity.this, MainActivity.class);
                    startActivity(intent);
                });
                goToMainAlert.show(getSupportFragmentManager(), getString(R.string.go_back_to_main_menu));
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

        TextView clockText = findViewById(R.id.clockText);
        clockText.setVisibility(GONE);
    }
    private void setImageButtonOnClicked(){
        announcementsButton.setOnClickListener(v -> {
            MessageFragment messageFragment = new MessageFragment(
                    gameService.getMessageService().getPlayerMessages(gameService.getCurrentPlayer()));

            messageFragment.show(getSupportFragmentManager(), getString(R.string.messages));
        });

        graveyardButton.setOnClickListener(v -> {
            GraveyardFragment graveyardFragment = new GraveyardFragment(
                    gameService.getDeadPlayers());

            graveyardFragment.show(getSupportFragmentManager(), getString(R.string.graveyard));
        });

        roleBookButton.setOnClickListener(v->{
            RoleBookFragment roleBookFragment = new RoleBookFragment(gameMode);
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
        template = String.format(Locale.ROOT, template, currentPlayer.getNumber());
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

            boolean isTimeChanged = gameService.passTurn();

            createPassTurnDialog();

            if(isTimeChanged){

                changeTimeUI();

            }

        });
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
        AnnouncementsFragment announcementsFragment = new AnnouncementsFragment(null,
                gameService.getMessageService().getDailyAnnouncements());
        announcementsFragment.setDayText(gameService.getTimeService().getTimeAndDay());
        announcementsFragment.show(getSupportFragmentManager(), "Start Day Announcements");
    }
}
