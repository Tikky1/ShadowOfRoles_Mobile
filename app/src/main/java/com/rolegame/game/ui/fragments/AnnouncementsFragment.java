package com.rolegame.game.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.gamestate.Time;
import com.rolegame.game.models.Message;
import com.rolegame.game.services.TimeService;
import com.rolegame.game.ui.adapters.MessagesViewAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class AnnouncementsFragment extends DialogFragment {

    private List<Message> announcements;
    private TimeService timeService;
    private String dayText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_announcements, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView dayCountText = view.findViewById(R.id.day_start_text);
        dayCountText.setText(dayText);


        Button passAnnouncementsBtn = view.findViewById(R.id.pass_announcements_button);
        passAnnouncementsBtn.setOnClickListener(v -> dismiss());

        RecyclerView announcementsRecyclerView = view.findViewById(R.id.announcements_recycler_view);
        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        MessagesViewAdapter messagesViewAdapter = new MessagesViewAdapter();

        messagesViewAdapter.setMessages(filterMessages());

        announcementsRecyclerView.setAdapter(messagesViewAdapter);

        hideSystemUI();

    }


    private List<Message> filterMessages(){
        Time currentTime = timeService.getTime();
        int currentDayCount = timeService.getDayCount();

        List<Message> filteredMessages = announcements.stream()
                .filter(message -> message.isPublic() && (
                        (currentTime == Time.NIGHT && message.isDay() && message.getDayCount() == currentDayCount) ||
                                (currentTime == Time.DAY && !message.isDay() && message.getDayCount() == currentDayCount - 1)
                ))
                .collect(Collectors.toList());

        return filteredMessages;
    }
    private void hideSystemUI() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }
    public void setAnnouncementsAndTimeService(List<Message> announcements, TimeService timeService) {
        this.announcements = announcements;
        this.timeService = timeService;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }
}
