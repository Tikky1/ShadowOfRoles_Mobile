package com.kankangames.shadowofroles.ui.activities.multidevice;

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

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.gamestate.TimeManager;
import com.kankangames.shadowofroles.managers.GameScreenImageManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientListenerManager;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.jsonobjects.GameData;
import com.kankangames.shadowofroles.networking.jsonobjects.PlayerInfo;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDataReceivedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;
import com.kankangames.shadowofroles.ui.activities.GameEndActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.adapters.PlayersViewAdapter;
import com.kankangames.shadowofroles.ui.alerts.GoToMainAlert;
import com.kankangames.shadowofroles.ui.fragments.GraveyardFragment;
import com.kankangames.shadowofroles.ui.fragments.MessageFragment;
import com.kankangames.shadowofroles.ui.fragments.RoleBookFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.AnnouncementsFragment;
import com.kankangames.shadowofroles.ui.helper.ClockService;

import java.util.Locale;

public class MultipleDeviceGameActivity extends BaseActivity implements ClockService.ClockUpdateListener {
    private GameData gameData;
    private RecyclerView alivePlayersView;

    private TextView timeText;
    private TextView nameText;
    private TextView numberText;
    private TextView roleText;
    private TextView clockText;

    private ImageButton announcementsButton;
    private ImageButton graveyardButton;
    private ImageButton roleBookButton;
    private ImageView backgroundImage;
    private final GameMode gameMode = GameMode.MULTIPLE_DEVICE;
    private Client client;
    private ClockService clockService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        client = ClientManager.getInstance().getClient();
        initializeClientListeners();

        if(gameData == null) gameData = (GameData) client.getDataProvider();

        clockService = new ClockService(this);
        initializeViews();
        setRoleText();
        setNumberText();
        setNameText();
        updateGameUI();

        setImageButtonOnClicked();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                GoToMainAlert goToMainAlert = new GoToMainAlert(()->{
                    Intent intent = new Intent(MultipleDeviceGameActivity.this, MainActivity.class);
                    startActivity(intent);
                });
                goToMainAlert.show(getSupportFragmentManager(), getString(R.string.go_back_to_main_menu));
            }
        });

    }

    private void initializeClientListeners(){
        ClientListenerManager listenerManager = client.getClientListenerManager();

        listenerManager.addListener(OnGameDataReceivedListener.class, receivedGameData -> {
                    gameData = receivedGameData;
                    runOnUiThread(this::updateGameUI);

                });
        listenerManager.addListener(OnGameStartingListener.class,dataProvider -> {
            gameData = (GameData) dataProvider;

        });
        listenerManager.addListener(OnGameEndedListener.class, endGameData -> {
            StartGameService.getInstance().setEndGameData(endGameData);
            Intent intent = new Intent(this, GameEndActivity.class);
            startActivity(intent);

        });
    }


    private void initializeViews(){
        alivePlayersView = findViewById(R.id.alivePlayersView);

        timeText = findViewById(R.id.timeText);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        roleText = findViewById(R.id.roleText);
        clockText = findViewById(R.id.clockText);

        backgroundImage = findViewById(R.id.backgroundImageGame);
        announcementsButton = findViewById(R.id.announcementsBtn);
        graveyardButton = findViewById(R.id.gravestoneBtn);
        roleBookButton = findViewById(R.id.roleBookBtn);

        Button passTurnButton = findViewById(R.id.pass_turn_button);
        passTurnButton.setVisibility(GONE);
    }
    private void setImageButtonOnClicked(){
        announcementsButton.setOnClickListener(v -> {
            MessageFragment messageFragment = new MessageFragment(
                    gameData.getMessages(), gameData.getCurrentPlayer());

            messageFragment.show(getSupportFragmentManager(), getString(R.string.messages));
        });

        graveyardButton.setOnClickListener(v -> {
            GraveyardFragment graveyardFragment = new GraveyardFragment(
                    gameData.getDeadPlayers());

            graveyardFragment.show(getSupportFragmentManager(), getString(R.string.graveyard));
        });

        roleBookButton.setOnClickListener(v->{
            RoleBookFragment roleBookFragment = new RoleBookFragment(gameMode);
            roleBookFragment.show(getSupportFragmentManager(), "Role Book");
        });
    }


    private void setTimeText(){
        String template = gameData.getTimeService().getTime() != Time.NIGHT ?
                getString(R.string.day) :
                getString(R.string.night);

        template = String.format(template, gameData.getTimeService().getDayCount());
        timeText.setText(template);
    }


    private void updateGameUI(){

        updateAlivePlayersUI();
        setTimeText();
        updateBackgroundImage();
        changeTimeUI();
        startTimerText();

    }

    private void startTimerText(){
        int time;
        switch (gameData.getTimeService().getTime()){
            case NIGHT:
                time = TimeManager.nightTime;
                break;
            case DAY:
                time = TimeManager.dayTime;
                break;
            case VOTING:
                time = TimeManager.votingTime;
                break;
            default:
                time = 20_000;
                break;
        }
        clockService.startTimer(time);
    }

    private void updateAlivePlayersUI(){
        PlayersViewAdapter playersViewAdapter = new PlayersViewAdapter(
                gameData.getTimeService().getTime(), gameData.getCurrentPlayer(), this);
        playersViewAdapter.setPlayers(gameData.getAlivePlayers());
        alivePlayersView.setAdapter(playersViewAdapter);
        alivePlayersView.setLayoutManager(new LinearLayoutManager(this));

        int maxHeight = getResources().getDimensionPixelSize(R.dimen.max_recycler_view_height);
        ViewGroup.LayoutParams params = alivePlayersView.getLayoutParams();
        params.height = maxHeight;
        alivePlayersView.setLayoutParams(params);
        alivePlayersView.setItemViewCacheSize(gameData.getAlivePlayers().size());
    }


    private void updateBackgroundImage(){
        GameScreenImageManager gameScreenImageManager = GameScreenImageManager.getInstance(this);
        Drawable image;
        switch (gameData.getTimeService().getTime()){
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

        updateBackgroundImage();
        setTimeText();

        switch (gameData.getTimeService().getTime()){
            case DAY:
            case NIGHT:
                createAnnouncementsDialog();
                break;
            case VOTING:
                break;
        }

    }

    private void createAnnouncementsDialog(){
        AnnouncementsFragment announcementsFragment = new AnnouncementsFragment(null);
        announcementsFragment.setAnnouncementsAndTimeService(gameData.getMessages(),
                gameData.getTimeService());
        announcementsFragment.setDayText(gameData.getTimeService().getTimeAndDay());
        announcementsFragment.show(getSupportFragmentManager(), "Start Day Announcements");
    }

    private void setNameText(){
        Player currentPlayer = gameData.getCurrentPlayer();
        String template = getString(R.string.player_name);
        template = template
                .replace("{playerName}", currentPlayer.getName());
        nameText.setText(template);
    }

    private void setNumberText(){
        Player currentPlayer = gameData.getCurrentPlayer();
        String template = getString(R.string.player_number);
        template = String.format(Locale.ROOT, template, currentPlayer.getNumber());
        numberText.setText(template);
    }

    private void setRoleText(){
        Player currentPlayer = gameData.getCurrentPlayer();
        roleText.setText(currentPlayer.getRole().getTemplate().getName());
    }


    @Override
    public void onTimeUpdate(int remainingTime) {
        runOnUiThread(()->{
            int seconds = remainingTime / 1000;
            clockText.setText(String.format("%d seconds remaining", seconds));
        });

    }

    @Override
    public void onTimeUp() {
        if(gameData.getTimeService().getTime()!=Time.DAY){
            Player chosenPlayer = gameData.getCurrentPlayer().getRole().getChoosenPlayer();
            int chosenPlayerNumber = chosenPlayer==null ? -1 : chosenPlayer.getNumber();
            client.sendPlayerInfo(new PlayerInfo(
                            gameData.getCurrentPlayer().getNumber(),
                            chosenPlayerNumber,
                            gameData.getCurrentPlayer().getRole().getTemplate()
                    ));
        }
    }

}
