package com.kankangames.shadowofroles.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.Message;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.ui.adapters.MessagesViewAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFragment extends HidingNavigationFragment {

    private final List<Message> messages;
    private final Player currentPlayer;

    public MessageFragment(List<Message> messages, Player currentPlayer) {
        this.messages = messages;
        this.currentPlayer = currentPlayer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message);

        ViewGroup root = dialog.findViewById(R.id.message_root);
        Button closeBtn = dialog.findViewById(R.id.close_message_button);
        closeBtn.setOnClickListener(v->closingAnimation(root));
        backgroundTransparent(dialog);
        openingAnimation(root);

        RecyclerView messagesView = dialog.findViewById(R.id.message_recycler_view);
        messagesView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        MessagesViewAdapter messagesViewAdapter = new MessagesViewAdapter();
        messagesViewAdapter.setMessages(messages.stream().filter(
                message -> message.isPublic()||message.getReceiver().getNumber() == currentPlayer.getNumber()).collect(Collectors.toList()));
        messagesView.setAdapter(messagesViewAdapter);


        return dialog;
    }

    @Override
    public int openingAnimationType() {
        return R.anim.messages_open;
    }

    @Override
    public int closingAnimationType() {
        return R.anim.messages_close;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message;
    }

}
