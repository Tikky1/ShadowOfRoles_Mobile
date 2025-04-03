package com.kankangames.shadowofroles.ui.adapters;

import static android.view.View.GONE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessagesViewAdapter extends RecyclerView.Adapter<MessagesViewAdapter.ViewHolderMessage> {

    private Map<TimePeriod, List<Message>> messages = new HashMap<>();
    private List<TimePeriod> timePeriods;

    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_message, parent, false);
        ViewHolderMessage viewHolder = new ViewHolderMessage(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position) {
        TimePeriod timePeriod = timePeriods.get(position);
        List<Message> messageList = messages.get(timePeriod);
        holder.setMessageTextView(timePeriod, messageList);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public MessagesViewAdapter(Map<TimePeriod, List<Message>> messages) {
        setMessages(messages);
    }

    public void setMessages(Map<TimePeriod, List<Message>> messages) {
        this.messages = messages;
        this.timePeriods = new ArrayList<>(messages.keySet());
        notifyDataSetChanged();
    }

    public static class ViewHolderMessage extends RecyclerView.ViewHolder {

        private final TextView messageTextView;
        private final RecyclerView recyclerView;


        public ViewHolderMessage(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            messageTextView = itemView.findViewById(R.id.message_text);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, 0, 0, 16);
                itemView.setLayoutParams(marginParams);
            }

        }

        public void setMessageTextView(TimePeriod timePeriod, List<Message> messages) {
            if(messages.isEmpty()){
                messageTextView.setVisibility(GONE);
                recyclerView.setVisibility(GONE);
                return;
            }
            TextManager textManager = TextManager.getInstance();
           messageTextView.setText(String.format(Locale.ROOT,
                   timePeriod.time() == Time.NIGHT ?
                           textManager.getText(R.string.night)
                   : textManager.getText(R.string.day), timePeriod.dayCount()));
           MessageListAdapter messageListAdapter = new MessageListAdapter(messages);

           recyclerView.setAdapter(messageListAdapter);
           recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));


        }
    }
}
