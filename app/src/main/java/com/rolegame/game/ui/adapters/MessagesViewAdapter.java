package com.rolegame.game.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.models.Message;

import java.util.LinkedList;
import java.util.List;

public class MessagesViewAdapter extends RecyclerView.Adapter<MessagesViewAdapter.ViewHolderMessage> {

    private List<Message> messages = new LinkedList<>();

    @NonNull
    @Override
    public ViewHolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_message, parent, false);
        ViewHolderMessage viewHolder = new ViewHolderMessage(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMessage holder, int position) {
        holder.setMessageText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public static class ViewHolderMessage extends RecyclerView.ViewHolder {

        private final TextView messageText;


        public ViewHolderMessage(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, 0, 0, 16);
                itemView.setLayoutParams(marginParams);
            }

        }

        public void setMessageText(Message message) {
            if(message.isPublic()){
                itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.public_message));
                messageText.setTextColor(Color.WHITE);
            }
            else{
                itemView.setBackground(ContextCompat.getDrawable(itemView.getContext(),R.drawable.private_message));
                messageText.setTextColor(Color.BLACK);
            }


            messageText.setText(message.getTimeAndDayCountAsString()+" " +message.getMessage());

        }
    }
}
