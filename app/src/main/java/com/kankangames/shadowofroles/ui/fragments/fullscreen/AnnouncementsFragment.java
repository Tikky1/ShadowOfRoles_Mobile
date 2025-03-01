package com.kankangames.shadowofroles.ui.fragments.fullscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.services.TimeService;
import com.kankangames.shadowofroles.ui.adapters.MessagesViewAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class AnnouncementsFragment extends FullScreenFragment {

    private List<Message> announcements;
    private TimeService timeService;
    private String dayText;

    public AnnouncementsFragment(OnClose onClose) {
        super(onClose);
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

    public void setAnnouncementsAndTimeService(List<Message> announcements, TimeService timeService) {
        this.announcements = announcements;
        this.timeService = timeService;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_announcements;
    }
}
