package com.kankangames.shadowofroles.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class BlackScreenFragment extends DialogFragment implements IFullScreenFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        hideSystemUIBackgroundBlack(this);

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCancelable(false);
            dialog.setOnKeyListener((dialogInterface, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);
        }
    }
}
