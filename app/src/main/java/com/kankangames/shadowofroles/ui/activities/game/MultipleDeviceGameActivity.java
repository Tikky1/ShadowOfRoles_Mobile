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

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimeManager;
import com.kankangames.shadowofroles.game.services.MessageService;
import com.kankangames.shadowofroles.ui.helper.GameScreenImageManager;
import com.kankangames.shadowofroles.utils.managers.InstanceClearer;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.gamestate.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.listeners.NetworkListenerManager;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.GameDTO;
import com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects.PlayerDTO;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameDataReceivedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameEndedListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnGameStartingListener;
import com.kankangames.shadowofroles.networking.listeners.clientlistener.OnQuitedGameListener;
import com.kankangames.shadowofroles.game.services.StartGameService;
import com.kankangames.shadowofroles.ui.activities.BaseActivity;
import com.kankangames.shadowofroles.ui.activities.GameEndActivity;
import com.kankangames.shadowofroles.ui.activities.MainActivity;
import com.kankangames.shadowofroles.ui.adapters.PlayersViewAdapter;
import com.kankangames.shadowofroles.ui.alerts.AlertProvider;
import com.kankangames.shadowofroles.ui.alerts.GoToMainAlert;
import com.kankangames.shadowofroles.ui.fragments.GraveyardFragment;
import com.kankangames.shadowofroles.ui.fragments.MessageFragment;
import com.kankangames.shadowofroles.ui.fragments.RoleBookFragment;
import com.kankangames.shadowofroles.ui.fragments.fullscreen.AnnouncementsFragment;
import com.kankangames.shadowofroles.ui.helper.ClockService;

import java.util.Locale;

public class MultipleDeviceGameActivity extends BaseActivity implements ClockService.ClockUpdateListener {
    private GameDTO gameDTO;
    private RecyclerView alivePlayersView;
    private TextView timeText, nameText, numberText, roleText, clockText;
    private ImageButton announcementsButton, graveyardButton, roleBookButton;
    private ImageView backgroundImage;
    private final GameMode gameMode = GameMode.MULTIPLE_DEVICE;
    private Client client;
    private ClockService clockService;
    private boolean activityChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        client = ClientManager.getInstance().getClient();
        initializeClientListeners();

        if(gameDTO == null) gameDTO = (GameDTO) client.getClientGameManager().getDataProvider();

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
//                    if(client.getClientLobbyManager().isHost()){
//                        client.getClientGameManager().
//                    }
                    InstanceClearer.clearInstances();
                });
                goToMainAlert.show(getSupportFragmentManager(), getString(R.string.go_back_to_main_menu));
            }
        });

    }

    private void initializeClientListeners(){
        NetworkListenerManager listenerManager = client.getClientListenerManager();

        listenerManager.addListener(OnGameDataReceivedListener.class, receivedGameDTO -> {
                    gameDTO = receivedGameDTO;
                    runOnUiThread(this::updateGameUI);

                });
        listenerManager.addListener(OnGameStartingListener.class,dataProvider -> {
            gameDTO = (GameDTO) dataProvider;

        });
        listenerManager.addListener(OnGameEndedListener.class, endGameData -> {
            if(activityChanged) return;
            activityChanged = true;
            StartGameService.getInstance().setEndGameData(endGameData);
            Intent intent = new Intent(this, GameEndActivity.class);
            startActivity(intent);

        });

        listenerManager.addListener(OnQuitedGameListener.class, new OnQuitedGameListener() {
            @Override
            public void onHostQuited() {
                AlertProvider.showGeneralAlert(MultipleDeviceGameActivity.this,
                        "Go to main menu","Host quited from game,",
                        ()->{
                            Intent intent = new Intent(MultipleDeviceGameActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        });
            }

            @Override
            public void onClientQuited() {

            }
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
                    gameDTO.getMessages());

            messageFragment.show(getSupportFragmentManager(), getString(R.string.messages));
        });

        graveyardButton.setOnClickListener(v -> {
            GraveyardFragment graveyardFragment = new GraveyardFragment(
                    gameDTO.getDeadPlayers());

            graveyardFragment.show(getSupportFragmentManager(), getString(R.string.graveyard));
        });

        roleBookButton.setOnClickListener(v->{
            RoleBookFragment roleBookFragment = new RoleBookFragment(gameMode);
            roleBookFragment.show(getSupportFragmentManager(), "Role Book");
        });
    }


    private void setTimeText(){
        String template = gameDTO.getTimeService().getTime() != Time.NIGHT ?
                getString(R.string.day) :
                getString(R.string.night);

        template = String.format(template, gameDTO.getTimeService().getDayCount());
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
        switch (gameDTO.getTimeService().getTime()){
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
                gameDTO.getTimeService().getTime(), gameDTO.getCurrentPlayer(), this);
        playersViewAdapter.setPlayers(gameDTO.getAlivePlayers());
        alivePlayersView.setAdapter(playersViewAdapter);
        alivePlayersView.setLayoutManager(new LinearLayoutManager(this));

        int maxHeight = getResources().getDimensionPixelSize(R.dimen.max_recycler_view_height);
        ViewGroup.LayoutParams params = alivePlayersView.getLayoutParams();
        params.height = maxHeight;
        alivePlayersView.setLayoutParams(params);
        alivePlayersView.setItemViewCacheSize(gameDTO.getAlivePlayers().size());
    }


    private void updateBackgroundImage(){
        GameScreenImageManager gameScreenImageManager = GameScreenImageManager.getInstance(this);
        Drawable image;
        switch (gameDTO.getTimeService().getTime()){
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

        switch (gameDTO.getTimeService().getTime()){
            case DAY:
            case NIGHT:
                createAnnouncementsDialog();
                break;
            case VOTING:
                break;
        }

    }

    private void createAnnouncementsDialog(){
        AnnouncementsFragment announcementsFragment
                = new AnnouncementsFragment(null, MessageService.getDailyAnnouncements(gameDTO.getMessages(), gameDTO.getTimeService().getTimePeriod()));
        announcementsFragment.setDayText(gameDTO.getTimeService().getTimeAndDay());
        announcementsFragment.show(getSupportFragmentManager(), "Start Day Announcements");
    }

    private void setNameText(){
        Player currentPlayer = gameDTO.getCurrentPlayer();
        String template = getString(R.string.player_name);
        template = template
                .replace("{playerName}", currentPlayer.getName());
        nameText.setText(template);
    }

    private void setNumberText(){
        Player currentPlayer = gameDTO.getCurrentPlayer();
        String template = getString(R.string.player_number);
        template = String.format(Locale.ROOT, template, currentPlayer.getNumber());
        numberText.setText(template);
    }

    private void setRoleText(){
        Player currentPlayer = gameDTO.getCurrentPlayer();
        roleText.setText(currentPlayer.getRole().getTemplate().getName());
    }


    @Override
    public void onTimeUpdate(int remainingTime) {
        runOnUiThread(()->{
            int seconds = remainingTime / 1000;
            clockText.setText(String.format(Locale.ROOT,getString(R.string.remaining_seconds), seconds));
        });

    }

    @Override
    public void onTimeUp() {
        if(gameDTO.getTimeService().getTime()!=Time.DAY){
            Player chosenPlayer = gameDTO.getCurrentPlayer().getRole().getChoosenPlayer();
            int chosenPlayerNumber = chosenPlayer==null ? -1 : chosenPlayer.getNumber();
            client.getClientGameManager().sendPlayerInfo(new PlayerDTO(
                            gameDTO.getCurrentPlayer().getNumber(),
                            chosenPlayerNumber,
                            gameDTO.getCurrentPlayer().getRole().getTemplate()
                    ));
        }
    }

}
