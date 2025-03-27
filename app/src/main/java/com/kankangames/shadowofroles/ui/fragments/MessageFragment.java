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
import com.kankangames.shadowofroles.gamestate.TimePeriod;
import com.kankangames.shadowofroles.models.message.Message;
import com.kankangames.shadowofroles.ui.adapters.MessagesViewAdapter;

import java.util.List;
import java.util.Map;

public class MessageFragment extends HidingNavigationFragment {

    private final Map<TimePeriod, List<Message>> messages;

    public MessageFragment(Map<TimePeriod, List<Message>> messages) {
        this.messages = messages;
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
        MessagesViewAdapter messagesViewAdapter = new MessagesViewAdapter(messages);
        messagesView.setAdapter(messagesViewAdapter);

        messagesView.scrollToPosition(messagesViewAdapter.getItemCount() - 1);


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
