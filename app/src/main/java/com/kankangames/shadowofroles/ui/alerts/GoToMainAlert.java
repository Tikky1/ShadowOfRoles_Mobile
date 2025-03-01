package com.kankangames.shadowofroles.ui.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.kankangames.shadowofroles.R;

public class GoToMainAlert extends DialogFragment {
    public interface GoBackToMenu {
        void goToMenu();
    }

    private final GoBackToMenu goBackToMenu;
    public GoToMainAlert(GoBackToMenu goBackToMenu){
        this.goBackToMenu = goBackToMenu;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to go to main menu")
                .setPositiveButton("Yes", (dialog, id) -> goBackToMenu.goToMenu())
                .setNegativeButton("No", (dialog, id) -> {

                });

        return builder.create();
    }
}