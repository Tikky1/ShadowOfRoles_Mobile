package com.kankangames.shadowofroles.ui.adapters;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.message.Message;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {

    public List<Message> messages;

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        holder.setMessageTextView(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public MessageListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public static class MessageListViewHolder extends RecyclerView.ViewHolder{

        private final TextView messageTextView;
        public MessageListViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
        }

        public void setMessageTextView(Message message) {
            if(message.isPublic()){
                itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.public_message));
                messageTextView.setTextColor(Color.WHITE);
            }
            else{
                itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.private_message));
                messageTextView.setTextColor(Color.BLACK);
            }


            String timeAndDay = message.getTimeAndDayCountAsString();
            String messageText = message.getMessage();

            SpannableString spannable = new SpannableString("[" + timeAndDay + "]\n" + messageText);

            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, timeAndDay.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new RelativeSizeSpan(1f), 0, timeAndDay.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            messageTextView.setText(spannable);

        }
    }


}
