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
import com.kankangames.shadowofroles.managers.GameScreenImageManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.networking.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.services.MultiDeviceGameService;
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

import java.util.Locale;

public class MultipleDeviceGameActivity extends BaseActivity {
    private MultiDeviceGameService gameService;
    private RecyclerView alivePlayersView;

    private TextView timeText;
    private TextView nameText;
    private TextView numberText;
    private TextView roleText;

    private ImageButton announcementsButton;
    private ImageButton graveyardButton;
    private ImageButton roleBookButton;
    private ImageView backgroundImage;
    private final GameMode gameMode = GameMode.MULTIPLE_DEVICE;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        gameService = (MultiDeviceGameService) StartGameService.getInstance().getGameService();

        client = ClientManager.getInstance().getClient();

        initializeViews();

//        changePlayerUI();
        setTimeText();
        setBackgroundImage();
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


    private void initializeViews(){
        alivePlayersView = findViewById(R.id.alivePlayersView);
        timeText = findViewById(R.id.timeText);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        roleText = findViewById(R.id.roleText);
        backgroundImage = findViewById(R.id.backgroundImageGame);
        announcementsButton = findViewById(R.id.announcementsBtn);
        graveyardButton = findViewById(R.id.gravestoneBtn);
        roleBookButton = findViewById(R.id.roleBookBtn);

        Button passTurnButton = findViewById(R.id.pass_turn_button);
        passTurnButton.setVisibility(GONE);
    }
    private void setImageButtonOnClicked(){
        announcementsButton.setOnClickListener(v -> {
//            MessageFragment messageFragment = new MessageFragment(
//                    gameService.getMessageService().getMessages(), client.getPlayer());

//            messageFragment.show(getSupportFragmentManager(), getString(R.string.messages));
        });

        graveyardButton.setOnClickListener(v -> {
            GraveyardFragment graveyardFragment = new GraveyardFragment(
                    gameService.getDeadPlayers());

            graveyardFragment.show(getSupportFragmentManager(), getString(R.string.graveyard));
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

//    private void setNameText(){
//        Player currentPlayer = client.getPlayer();
//        String template = getString(R.string.player_name);
//        template = template
//                .replace("{playerName}", currentPlayer.getName());
//        nameText.setText(template);
//    }
//
//    private void setNumberText(){
//        Player currentPlayer = client.getPlayer();
//        String template = getString(R.string.player_number);
//        template = String.format(Locale.ROOT, template, currentPlayer.getNumber());
//        numberText.setText(template);
//    }
//
//    private void setAlivePlayersView(){
//        PlayersViewAdapter playersViewAdapter = new PlayersViewAdapter(
//                gameService.getTimeService().getTime(), client.getPlayer(), this);
//        playersViewAdapter.setPlayers(gameService.getAlivePlayers());
//        alivePlayersView.setAdapter(playersViewAdapter);
//        alivePlayersView.setLayoutManager(new LinearLayoutManager(this));
//
//        int maxHeight = getResources().getDimensionPixelSize(R.dimen.max_recycler_view_height);
//        ViewGroup.LayoutParams params = alivePlayersView.getLayoutParams();
//        params.height = maxHeight;
//        alivePlayersView.setLayoutParams(params);
//        alivePlayersView.setItemViewCacheSize(gameService.getAlivePlayers().size());
//    }
//
//    private void setRoleText(){
//        Player currentPlayer = client.getPlayer();
//        roleText.setText(currentPlayer.getRole().getTemplate().getName());
//    }

//    private void changePlayerUI(){
//        setNameText();
//        setNumberText();
//        setRoleText();
//        setAlivePlayersView();
//    }


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

    private void createAnnouncementsDialog(){
        AnnouncementsFragment announcementsFragment = new AnnouncementsFragment(()->{});
        announcementsFragment.setAnnouncementsAndTimeService(gameService.getMessageService().getMessages(),
                gameService.getTimeService());
        announcementsFragment.setDayText(gameService.getTimeService().getTimeAndDay());
        announcementsFragment.show(getSupportFragmentManager(), "Start Day Announcements");
    }


//    @Override
//    public void onGameServiceUpdate(MultiDeviceGameService gameService) {
//
//    }
}
