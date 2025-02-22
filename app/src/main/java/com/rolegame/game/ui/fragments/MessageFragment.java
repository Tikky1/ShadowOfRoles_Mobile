package com.rolegame.game.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.models.Message;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.ui.adapters.MessagesViewAdapter;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class MessageFragment extends DialogFragment {

    private final LinkedList<Message> messages;
    private final Player currentPlayer;

    public MessageFragment(LinkedList<Message> messages, Player currentPlayer) {
        this.messages = messages;
        this.currentPlayer = currentPlayer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message);

        RecyclerView messagesView = dialog.findViewById(R.id.message_recycler_view);

        messagesView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        MessagesViewAdapter messagesViewAdapter = new MessagesViewAdapter();

        messagesViewAdapter.setMessages(messages.stream().filter(message -> message.isPublic()||message.getReceiver().getNumber() == currentPlayer.getNumber()).collect(Collectors.toList()));

        messagesView.setAdapter(messagesViewAdapter);


        return dialog;
    }
}
