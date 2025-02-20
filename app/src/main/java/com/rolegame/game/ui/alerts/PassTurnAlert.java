package com.rolegame.game.ui.alerts;

import android.content.DialogInterface;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class PassTurnAlert {
    public void showConfirmationDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("You are passing your turn are you sure?")
                .setMessage("Pass")
                .setCancelable(false)
                .setPositiveButton("Pass Turn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

