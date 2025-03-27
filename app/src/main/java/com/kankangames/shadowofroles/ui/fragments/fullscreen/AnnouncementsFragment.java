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
import com.kankangames.shadowofroles.gamestate.TimePeriod;
import com.kankangames.shadowofroles.models.message.Message;
import com.kankangames.shadowofroles.ui.adapters.MessagesViewAdapter;

import java.util.List;
import java.util.Map;

public class AnnouncementsFragment extends FullScreenFragment {

    private Map<TimePeriod, List<Message>> announcements;
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
        MessagesViewAdapter messagesViewAdapter = new MessagesViewAdapter(announcements);

        announcementsRecyclerView.setAdapter(messagesViewAdapter);

    }

    public void setAnnouncements(Map<TimePeriod, List<Message>> announcements) {
        this.announcements = announcements;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_announcements;
    }
}
